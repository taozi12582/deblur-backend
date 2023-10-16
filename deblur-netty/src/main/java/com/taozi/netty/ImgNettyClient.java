package com.taozi.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.concurrent.ForkJoinPool;

@Slf4j
@Component
public class ImgNettyClient {

    private final EventLoopGroup group = new NioEventLoopGroup();
    private ChannelFuture mChannelFuture = null;
    private final ThreadLocal<Channel> mChannel = new ThreadLocal<>();

    @Resource
    private ImgNettyClientHandler imgNettyClientHandler;

    public void startClient(String host, int port) {
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast("c1", new DelimiterBasedFrameDecoder(1024000000, Unpooled.copiedBuffer("mscg"
                                    .getBytes(StandardCharsets.UTF_8))));
                            p.addLast(imgNettyClientHandler);
                        }
                    });

            mChannelFuture = b.connect(host, port).addListener(future -> {
                log.info(String.format("客户端启动成功，并监听端口：%s ", port));
            });
        } catch (Exception e) {
            log.error("启动 netty 客户端出现异常", e);
        }
    }

    /**
     * 客户端通过 Channel 对象向服务器端发送数据
     *
     * @param imgPath 图片绝对路径
     */
    public void send(String imgPath) {
        try {
            System.out.println();
            if (mChannel.get() == null) {
                mChannel.set(mChannelFuture.channel());
            }
            byte[] imageBytes = Files.readAllBytes(Paths.get(imgPath));
            byte[] fileData = Base64.getEncoder().encode(imageBytes);
            byte[] separator = "mscg".getBytes(StandardCharsets.UTF_8);
            byte[] imageData = new byte[fileData.length + separator.length];
            System.arraycopy(fileData, 0, imageData, 0, fileData.length);
            System.arraycopy(separator, 0, imageData, fileData.length, separator.length);
            System.out.println("发送数据长度：" + imageData.length);
            int chunkSize = 2048;
            int offset = 0;
            while (offset < imageData.length) {
                int length = Math.min(chunkSize, imageData.length - offset);
                byte[] chunk = new byte[length];
                System.arraycopy(imageData, offset, chunk, 0, length);
                mChannel.get().writeAndFlush(Unpooled.copiedBuffer(chunk)).sync();
                offset += length;
            }
        } catch (Exception e) {
            log.error(this.getClass().getName().concat(".send has error"), e);
        }
    }

    @PostConstruct
    public void init() {
        ForkJoinPool.commonPool().submit(() -> startClient("202.115.17.206", 8888));
    }

    @PreDestroy
    public void destroy() {
        System.out.println("已经关闭");
        group.shutdownGracefully();
    }

}

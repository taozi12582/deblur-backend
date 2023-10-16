package com.taozi.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Component
@ChannelHandler.Sharable
@Slf4j
public class ImgNettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf decodedImage) {
        String outputPath = "/stdStorage/taozi/deblur-backend/deblur-netty/src/main/java/com/taozi/netty/image.jpg"; // 替换为实际的保存路径
        byte[] bytes = new byte[decodedImage.readableBytes()];
        decodedImage.readBytes(bytes);
        System.out.println(bytes.length);
        byte[] imageDecode = Base64.getDecoder().decode(bytes);
        try {
            // 将解码后的字节数组保存到本地文件
            Files.write(Paths.get(outputPath), imageDecode);
            System.out.println("图片保存成功到：" + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("图片保存失败");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


}

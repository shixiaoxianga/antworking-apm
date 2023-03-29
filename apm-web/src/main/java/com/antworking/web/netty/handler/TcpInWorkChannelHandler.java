package com.antworking.web.netty.handler;

import com.antworking.model.grpc.BaseMessageProto;
import com.antworking.web.netty.client.ClientTools;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * describe：服务端入站处理
 *
 * @author Xiang
 * date 2022/2/8
 */
@Slf4j
@ChannelHandler.Sharable
public class TcpInWorkChannelHandler extends SimpleChannelInboundHandler<BaseMessageProto.BaseMessage> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端建立连接，当前时间：{}", System.currentTimeMillis());
        ClientTools.addChannel(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("解码发生异常:{}", cause.getMessage());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseMessageProto.BaseMessage msg) throws Exception {
        log.info(msg.toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("客户端断开连接：{}", ctx.channel().id());
        ClientTools.removeChannel(ctx.channel().id().asLongText());
        super.channelInactive(ctx);
    }
}

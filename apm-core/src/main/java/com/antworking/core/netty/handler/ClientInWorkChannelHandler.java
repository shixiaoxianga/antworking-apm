package com.antworking.core.netty.handler;

import com.antworking.core.netty.ClientRun;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import com.antworking.model.grpc.BaseMessageProto;
import com.google.protobuf.ByteString;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * describe：入站处理
 *
 * @author Xiang
 * date 2022/2/7
 */
public class ClientInWorkChannelHandler extends SimpleChannelInboundHandler<BaseMessageProto.BaseMessage> {

    private final AwLog log = LoggerFactory.getLogger(ClientInWorkChannelHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端与服务器建立连接，当前时间：{}", System.currentTimeMillis());
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("解码发生异常：{}", cause.getMessage());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseMessageProto.BaseMessage msg) throws Exception {
        ctx.channel().flush();
        ctx.fireChannelRead(msg);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("客户端断开连接：{}", ctx.channel().id());
        super.channelInactive(ctx);
    }
}

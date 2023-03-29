package com.antworking.web.netty.handler;

import com.antworking.model.grpc.BaseMessageProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

/**
 * describe：出站处理
 * @author Xiang
 * date 2022/2/7
 */
@Slf4j
public class TcpOutWorkChannelHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        BaseMessageProto.BaseMessage message = (BaseMessageProto.BaseMessage)msg;
        ctx.flush();
    }
}

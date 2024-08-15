//package com.antworking.core.netty.handler;
//
//import com.antworking.model.grpc.BaseMessageProto;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelOutboundHandlerAdapter;
//import io.netty.channel.ChannelPromise;
//
///**
// * describe：出站处理
// * @author Xiang
// * date 2022/2/7
// */
//public class ClientOutWorkChannelHandler extends ChannelOutboundHandlerAdapter {
//    @Override
//    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//        BaseMessageProto.BaseMessage medium = (BaseMessageProto.BaseMessage)msg;
//        ctx.writeAndFlush(medium);
//    }
//}

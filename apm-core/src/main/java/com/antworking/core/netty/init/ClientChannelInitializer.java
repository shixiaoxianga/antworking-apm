//package com.antworking.core.netty.init;
//
//import com.antworking.core.netty.handler.ClientInWorkChannelHandler;
//import com.antworking.model.grpc.BaseMessageProto;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.handler.codec.protobuf.ProtobufDecoder;
//import io.netty.handler.codec.protobuf.ProtobufEncoder;
//import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
//import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
//
///**
// * describe：初始化客户端channel处理
// * @author Xiang
// * date 2022/2/8
// */
//public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
//
//    @Override
//    protected void initChannel(SocketChannel channel) throws Exception {
//        channel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
//        channel.pipeline().addLast(new ProtobufDecoder(BaseMessageProto.BaseMessage.getDefaultInstance()));
//        channel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
//        channel.pipeline().addLast(new ProtobufEncoder());
//        channel.pipeline().addLast("work-in",new ClientInWorkChannelHandler());
////        channel.pipeline().addLast("work-out",new ClientOutWorkChannelHandler());
//    }
//}

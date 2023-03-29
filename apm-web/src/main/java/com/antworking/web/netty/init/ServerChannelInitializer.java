package com.antworking.web.netty.init;

import com.antworking.model.grpc.BaseMessageProto;
import com.antworking.web.netty.handler.TcpInWorkChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * describe：初始化服务端channel处理
 *
 * @author Xiang
 * date 2022/2/8
 */
@Slf4j
@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        channel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
        channel.pipeline().addLast(new ProtobufDecoder(BaseMessageProto.BaseMessage.getDefaultInstance()));
        channel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
        channel.pipeline().addLast(new ProtobufEncoder());
        channel.pipeline().addLast("work-in", new TcpInWorkChannelHandler());
//        channel.pipeline().addLast("work-out",new TcpOutWorkChannelHandler());
    }
}

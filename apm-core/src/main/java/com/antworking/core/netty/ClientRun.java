package com.antworking.core.netty;

import com.antworking.core.netty.init.ClientChannelInitializer;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import com.antworking.model.grpc.BaseMessageProto;
import com.google.protobuf.ByteString;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

public class ClientRun {
    private final AwLog log = LoggerFactory.getLogger(ClientRun.class);

    public static Channel channel;

    public void run() {
        SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 2002);
        EventLoopGroup mainGroup = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(mainGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ClientChannelInitializer());
        bootstrap.remoteAddress("127.0.0.1", 2002);
        try {
            final ChannelFuture future = bootstrap.connect(socketAddress);
            log.info("客户端启动成功");
            ClientRun.channel = future.sync().channel();
//            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mainGroup.shutdownGracefully();
        }
    }
}

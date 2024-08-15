//package com.antworking.web.netty.factory;
//
//import com.antworking.web.netty.config.NettyServerConfig;
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.logging.LogLevel;
//import io.netty.handler.logging.LoggingHandler;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//import java.net.InetSocketAddress;
//import java.net.SocketAddress;
//
///**
// * describe：生产netty所需对象
// * @author Xiang
// * date 2022/2/8
// */
//@Component
//public class NettyBeanFactory {
//
//    @Autowired
//    private NettyServerConfig nettyServerConfig;
//    @Autowired
//    private ServerChannelInitializer serverChannelInitializer;
//
//    @Bean
//    public SocketAddress socketAddress(){
//        return  new InetSocketAddress(nettyServerConfig.getServerHost(), nettyServerConfig.getServerPort());
//    }
//
//    @Bean(destroyMethod = "shutdownGracefully")
//    public EventLoopGroup bossGroup(){
//        return new NioEventLoopGroup(nettyServerConfig.getBoosThread());
//    }
//
//    @Bean(destroyMethod = "shutdownGracefully")
//    public EventLoopGroup workGroup(){
//        return new NioEventLoopGroup(nettyServerConfig.getWorkThread());
//    }
//
//    @Bean
//    public ServerBootstrap serverBootstrap(SocketAddress socketAddress,EventLoopGroup bossGroup,EventLoopGroup workGroup){
//        ServerBootstrap bootstrap = new ServerBootstrap();
//        bootstrap.group(bossGroup,workGroup);
//        bootstrap.channel(NioServerSocketChannel.class);
//        bootstrap.handler(new LoggingHandler(LogLevel.INFO));
//        bootstrap.childHandler(serverChannelInitializer);
//        return bootstrap;
//    }
//
//
//
//}

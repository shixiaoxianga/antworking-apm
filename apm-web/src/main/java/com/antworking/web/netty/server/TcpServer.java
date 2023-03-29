package com.antworking.web.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;

/**
 * describe：TcpServer
 *
 * @author Xiang
 * date 2022/2/8
 */
@Slf4j
@Component
public class TcpServer implements CommandLineRunner {

    @Autowired
    private ServerBootstrap serverBootstrap;

    @Autowired
    private SocketAddress socketAddress;

    private Channel channel;

    public void start() {
        try {
            channel = serverBootstrap.bind(socketAddress).sync().channel();
            log.info("TCP服务开启成功~~");
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("tcp服务启动失败：{}", e.getMessage());
        } finally {
            stop();
            log.debug("invoke stop method~~");
        }
    }

    /**
     * describe：关闭channel以及父级
     *
     * @author Xiang
     * date 2022/2/8
     */
    public void stop() {
        channel.parent().close();
        channel.close();
    }

    @Override
    public void run(String... args) throws Exception {
        start();
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }
}

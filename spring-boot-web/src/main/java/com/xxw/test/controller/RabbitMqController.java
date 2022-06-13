package com.xxw.test.controller;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping
public class RabbitMqController {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 	这里就是确认消息的回调监听接口，用于确认消息是否被broker所收到
     */
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        /**
         * 	@param ack broker 是否落盘成功
         * 	@param cause 失败的一些异常信息
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.err.println("消息ACK结果:" + ack + ", correlationData: " + correlationData.getId());
        }
    };



    @RequestMapping("mqtest")
    public String mqTest(){
        MessageHeaders mhs = new MessageHeaders(new HashMap<>());
        Message<?> msg = MessageBuilder.createMessage("hello word", mhs);

        rabbitTemplate.setConfirmCallback(confirmCallback);

        // 	指定业务唯一的iD
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

        MessagePostProcessor mpp = new MessagePostProcessor() {

            @Override
            public org.springframework.amqp.core.Message postProcessMessage(org.springframework.amqp.core.Message message)
                    throws AmqpException {
                System.err.println("---> 发送的消息: " + message);
                return message;
            }
        };
        //1、交换机  2、路由 3、消息 4、处理 5、唯一
        rabbitTemplate.convertAndSend("exchange-1", "springboot.rabbit", "hello word".getBytes(), mpp, correlationData);
        rabbitTemplate.convertAndSend("exchange-1", "springboot.rabbit", "哇好牛逼啊".getBytes(), mpp, correlationData);
        rabbitTemplate.convertAndSend("exchange-1", "springboot.rabbit", "太厉害了吧~".getBytes(), mpp, correlationData);
        return "success";
    }

}

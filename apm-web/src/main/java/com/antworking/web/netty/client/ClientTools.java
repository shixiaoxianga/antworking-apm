package com.antworking.web.netty.client;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * describe：操作客户端连接工具
 * @author Xiang
 * date 2022/2/7
 */
public class ClientTools {

    private static final Map<String,Channel> channelMap = new ConcurrentHashMap<>();

    /**
     * describe：获取Channel
     * @author Xiang
     * date 2022/2/7
     */
    public static Channel getChannel(String channelId){
        return channelMap.get(channelId);
    }

    /**
     * describe：获取此连接的机器信息
     * @author Xiang
     * date 2022/2/7
     */
    public static Object getChannelInfo(){
        return null;
    }

    /**
     * describe：删除Channel
     * @author Xiang
     * date 2022/2/7
     */
    public static void removeChannel(String channelId){
        channelMap.remove(channelId);
    }
    /**
     * describe：添加连接
     * @author Xiang
     * date 2022/2/7
     */
    public static void addChannel(Channel channel){
        channelMap.put(channel.id().asLongText(),channel);
    }

    /**
     * describe：群发消息
     * @author Xiang
     * date 2022/2/7
     */
    public static void sendAllMsg(Object msg){
        channelMap.forEach((key, value) -> value.writeAndFlush(msg));
    }

    /**
     * describe：给指定channel发送消息
     * @author Xiang
     * date 2022/2/7
     * @return
     */
    public static boolean sendMsg(String channelId, Object msg){
        final Channel channel = channelMap.get(channelId);
        if(channel!=null){
            channel.writeAndFlush(msg);
            return true;
        }
        return false;
    }
    /**
     * describe：给指定channel发送消息
     * @author Xiang
     * date 2022/2/7
     * @return
     */
    public static void sendMsg(Channel channel, Object msg){
        channel.writeAndFlush(msg);
    }


    /**
     * describe：查询连接总数
     * @author Xiang
     * date 2022/2/8
     */
    public Integer getSize(){
        return channelMap.size();
    }
}

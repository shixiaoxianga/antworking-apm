package com.xxw.test.controller;

import com.xxw.test.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.net.www.http.HttpClient;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequestMapping("/")
@RestController
public class IndexController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserDao userDao;
    @Transactional
    @GetMapping("test2")
    public String test2(String a){
        userDao.findUser("1","xxw");
        System.out.println("test2-----------1");
        userDao.findUserById("2");
        System.out.println("test2-----------2");
        userDao.insertUser(UUID.randomUUID().toString(),"老外","10");
        System.out.println("test2-----------2");
        userDao.insertUser(UUID.randomUUID().toString(),"测试","11");
        System.out.println("test3-----------3");
        return a;
    }

    @GetMapping("test1")
    public void test1(){


        userDao.findUser("1","xxw");
        System.out.println("test1-----------1");
        userDao.findUserById("2");
        System.out.println("test1-----------2");
        userDao.insertUser(UUID.randomUUID().toString(),"老外","10");
        System.out.println("test1---------------3");
    }

    @GetMapping("sendHttp")
    public void send(){

        HttpUtils.sendGet("http://127.0.0.1:6060/test1","");
        HttpUtils.sendGet("http://127.0.0.1:6060/test2","");

        //发送Http请求
        HttpUtils.sendGet("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm","tel=131760");


        HttpUtils.sendPost("http://qzone-music.qq.com/fcg-bin/cgi_playlist_xml.fcg?uin=QQ号码&json=1&g_tk=1916754934","");


        HttpUtil httpUtil = new HttpUtil();
        HashMap map = new HashMap();
        map.put("tel","131760");
        httpUtil.sendGet("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm",map);
        httpUtil.sendPost("http://qzone-music.qq.com/fcg-bin/cgi_playlist_xml.fcg?uin=QQ号码&json=1&g_tk=1916754934",new HashMap<>());
    }


    @GetMapping("errortest")
    public void error(){
        System.out.println("error...................");
       throw new RuntimeException("模拟异常");

    }
    @GetMapping("x")
    public void thread(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {
            System.out.println("run...");
        });
    }
    @GetMapping("redis")
    public void redis(){
     redisTemplate.opsForValue().set("name","Xiang");
        System.out.println(redisTemplate.opsForValue().get("name"));
        redisTemplate.opsForList().leftPush("listkey","index1Val");
        System.out.println(redisTemplate.opsForList().index("listkey", 0));
        new Thread(()->{
            System.out.println(redisTemplate.opsForValue().get("name"));
        }).start();
    }

    public void a() throws IOException {
/*        final HttpClient aNew = HttpClient.New();
        aNew.parseHTTP()
        HttpURLConnection connection = new HttpURLConnection();
        connection.connect();
        connection.getOutputStream().write();*/
    }

}

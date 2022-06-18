package com.xxw.test.controller;

import com.xxw.test.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.net.www.http.HttpClient;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.IOException;
import java.util.UUID;

@RequestMapping("/")
@RestController
public class IndexController {

    @Autowired
    private UserDao userDao;
    @Transactional
    @GetMapping("test2")
    public String test2(String a){
        userDao.findUser("1","xxw");
        System.out.println("-------------------");
        userDao.findUserById("2");
        System.out.println("-------------------");
        userDao.insertUser(UUID.randomUUID().toString(),"老外","10");
        System.out.println("-------------------");
        return a;
    }

    @GetMapping("test1")
    public void test1(){


        userDao.findUser("1","xxw");
        System.out.println("-------------------");
        userDao.findUserById("2");
        System.out.println("-------------------");
        userDao.insertUser(UUID.randomUUID().toString(),"老外","10");
        System.out.println("-------------------");
    }

    @GetMapping("sendHttp")
    public void send(){
        //发送Http请求
        HttpUtils.sendGet("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm","tel=131760");

        HttpUtils.sendPost("http://qzone-music.qq.com/fcg-bin/cgi_playlist_xml.fcg?uin=QQ号码&json=1&g_tk=1916754934","");

    }


    @GetMapping("errortest")
    public void error(){
        System.out.println("error...................");
       throw new RuntimeException("模拟异常");

    }

    public void a() throws IOException {
/*        final HttpClient aNew = HttpClient.New();
        aNew.parseHTTP()
        HttpURLConnection connection = new HttpURLConnection();
        connection.connect();
        connection.getOutputStream().write();*/
    }

}

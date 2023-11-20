package com.lld.im.tcp;

import com.lld.im.codec.config.BootstrapConfig;
import com.lld.im.tcp.reciver.MessageReceiver;
import com.lld.im.tcp.redis.RedisManager;
import com.lld.im.tcp.register.RegistryZK;
import com.lld.im.tcp.server.LimServer;
import com.lld.im.tcp.server.LimWebSocketServer;
import com.lld.im.tcp.utils.MqFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

/**
 * @description:
 * @author: lld
 * @version: 1.0
 */
public class Starter {


//    HTTP GET POST PUT DELETE 1.0 1.1 2.0
    //client IOS 安卓 pc(windows mac) web //支持json 也支持 protobuf
    //appId
    //28 + imei + body
    //请求头（指令 版本 clientType 消息解析类型 imei长度 appId bodylen）+ imei号 + 请求体
    //len+body


    public static void main(String[] args)  {

            start();

    }

    private static void start(){
        try {
            Yaml yaml = new Yaml();
            InputStream resourceAsStream = Starter.class.getResourceAsStream("/config.yml");
            BootstrapConfig bootstrapConfig = yaml.loadAs(resourceAsStream, BootstrapConfig.class);

            new LimServer(bootstrapConfig.getLim()).start();
            new LimWebSocketServer(bootstrapConfig.getLim()).start();

            RedisManager.init(bootstrapConfig);
            MqFactory.init(bootstrapConfig.getLim().getRabbitmq());
            MessageReceiver.init(bootstrapConfig.getLim().getBrokerId()+"");

           new Thread(new RegistryZK(bootstrapConfig)).start();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(500);
        }
    }

}

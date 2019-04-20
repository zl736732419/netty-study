package com.zheng.netty.serialize;

import com.google.protobuf.InvalidProtocolBufferException;
import com.zheng.netty.serialize.protobuf.PersonModule;
import com.zheng.netty.serialize.protobuf.PlayerModule;
import org.junit.Test;

import java.util.Arrays;

/**
 * @Author zhenglian
 * @Date 2019/4/16
 */
public class ProtobufTest {
    @Test
    public void palyerTest() {
        PlayerModule.Player player = PlayerModule.Player.newBuilder()
                .setId(1).setAge(20).setName("zhangsan").build();
        byte[] bytes = player.toByteArray();
        System.out.println(Arrays.toString(bytes));
        try {
            PlayerModule.Player player1 = PlayerModule.Player.parseFrom(bytes);
            System.out.println(player1);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        /*
        protobuf为什么字节那么少？
        1. protobuf将类信息保存到配置文件中，无需将这些信息写入字节数组
        2. protobuf会对保存的对象属性按照实际占用的内存大小分配内存，比如int,可能只占用了2个字节，
        那么其他的两个字节就会被复用来表示其他字段值，整体存储结构会看起来很紧凑
        
        输出结果:
        [8, 1, 16, 20, 26, 8, 122, 104, 97, 110, 103, 115, 97, 110]
        id: 1
        age: 20
        name: "zhangsan"
        */
    }
    
    @Test
    public void personTest() throws Exception {
        PersonModule.Person.PhoneNumber number =
                PersonModule.Person.PhoneNumber.newBuilder()
                .setNumber("1234567")
                .setType(PersonModule.Person.PhoneType.MOBILE)
                .build();
        PersonModule.Person person = PersonModule.Person.newBuilder()
                .setName("张三")
                .setId(1)
                .setEmail("1234566@qq.com")
                .addPhone(number)
                .build();

        byte[] bytes = person.toByteArray();
        System.out.println(Arrays.toString(bytes));
        PersonModule.Person person1 = PersonModule.Person.parseFrom(bytes);
        System.out.println(person1);
    }
    
}

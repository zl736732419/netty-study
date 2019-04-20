package com.zheng.netty.protobuf;

import com.zheng.netty.serialize.protobuf.OneofApp;
import org.junit.Test;

/**
 * @Author zhenglian
 * @Date 2019/4/19
 */
public class OneofTest {
    @Test
    public void test() {
        OneofApp.Oneof oneof = OneofApp.Oneof.newBuilder()
                .setName("zhangsan")
                .setDesc("hello")
                .setAge(20)
                .setLevel(1)
                .build();
        System.out.println("name:" + oneof.hasName()); // false
        System.out.println("age:" + oneof.hasAge()); // false
    }
}

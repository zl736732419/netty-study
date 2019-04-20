package com.zheng.netty.protobuf;

import com.zheng.netty.serialize.protobuf.ExtensionApp1;
import com.zheng.netty.serialize.protobuf.FooApp;
import com.zheng.netty.serialize.protobuf.FooApp1;
import org.junit.Test;

/**
 * @Author zhenglian
 * @Date 2019/4/19
 */
public class ExtensionTest {
    
    @Test
    public void test() {
        FooApp.Foo foo = FooApp.Foo.newBuilder()
                .setName("zhangsan")
                .setExtension(FooApp.age, 20)
                .build();
        System.out.println(foo);
    }
    
    @Test
    public void test2() {
        FooApp1.Foo foo = FooApp1.Foo.newBuilder()
                .setName("hello")
                .setExtension(ExtensionApp1.age, 20)
                .build();
        ExtensionApp1.Extension extension = ExtensionApp1.Extension.newBuilder()
                .setId(1)
                .build();
        System.out.println(foo);
        System.out.println(extension);
    }
}

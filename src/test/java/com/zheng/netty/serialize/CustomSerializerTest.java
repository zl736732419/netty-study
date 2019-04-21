package com.zheng.netty.serialize;

import com.zheng.netty.model.Player;
import com.zheng.netty.model.Resource;
import org.junit.Test;

import java.util.Arrays;

/**
 * @Author zhenglian
 * @Date 2019/4/21
 */
public class CustomSerializerTest {
    @Test
    public void test() {
        Player player = new Player();
        player.setPlayerId(10001L);
        player.setName("小张");
        player.getSkills().add(101);
        Resource resource = new Resource();
        resource.setGold(99999);
        player.setResource(resource);

        byte[] bytes = player.getBytes();
        System.out.println(Arrays.toString(bytes));

        //==============================================

        Player player2 = new Player();
        player2.readFromBytes(bytes);
        System.out.println(player2);
    }
}

package com.zheng.netty.serialize;

import com.zheng.netty.model.Player;
import com.zheng.netty.model.Resource;
import com.zheng.netty.serialize.protobuf.PBPlayer;
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
        /*
        
        [0, 0, 0, 0, 0, 0, 39, 17, 0, 6, -27, -80, -113, -27, -68, -96, 0, 1, 0, 0, 0, 101, 1, 0, 1, -122, -97]
        Player{playerId=10001, name='小张', skills=[101], resource=Resource{gold=99999}}
        
        * */
    }
    
    @Test
    public void test2() throws Exception {
        PBPlayer.Resource resource = PBPlayer.Resource.newBuilder()
                .setGold(99999)
                .build();
        PBPlayer.Player player = PBPlayer.Player.newBuilder()
                .setPlayerId(10001L)
                .setName("小张")
                .addSkills(101)
                .setResource(resource)
                .build();
        byte[] bytes = player.toByteArray();
        System.out.println(Arrays.toString(bytes));

        PBPlayer.Player player2 = PBPlayer.Player.parseFrom(bytes);
        System.out.println(player2);
        
        /*
        [8, -111, 78, 18, 6, -27, -80, -113, -27, -68, -96, 24, 101, 34, 4, 8, -97, -115, 6]
        playerId: 10001
        name: "\345\260\217\345\274\240"
        skills: 101
        resource {
          gold: 99999
        }
        */
    }
    
}

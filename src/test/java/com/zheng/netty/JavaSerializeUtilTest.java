package com.zheng.netty;

import com.zheng.netty.serialize.JavaSerializeUtil;
import com.zheng.netty.serialize.Player;
import org.junit.Test;

import java.util.Arrays;

/**
 * @Author zhenglian
 * @Date 2019/4/16
 */
public class JavaSerializeUtilTest {
    
    @Test
    public void test() {
        Player player = new Player();
        player.setId(1);
        player.setName("xiaozhang");
        player.setAge(20);
        player.getSkills().add(1011);

        byte[] bytes = JavaSerializeUtil.serialize(player);
        System.out.println(Arrays.toString(bytes));
        Player player1 = JavaSerializeUtil.deserialize(bytes);
        System.out.println(player1);
        
        /*
        输出结果:
        [-84, -19, 0, 5, 115, 114, 0, 32, 99, 111, 109, 46, 122, 104, 101, 110, 103, 46, 110, 101, 116, 116, 121, 46, 115, 101, 114, 105, 97, 108, 105, 122, 101, 46, 80, 108, 97, 121, 101, 114, -85, -119, 10, 47, -36, -20, -21, -16, 2, 0, 4, 76, 0, 3, 97, 103, 101, 116, 0, 19, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 73, 110, 116, 101, 103, 101, 114, 59, 76, 0, 2, 105, 100, 113, 0, 126, 0, 1, 76, 0, 4, 110, 97, 109, 101, 116, 0, 18, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 76, 0, 6, 115, 107, 105, 108, 108, 115, 116, 0, 16, 76, 106, 97, 118, 97, 47, 117, 116, 105, 108, 47, 76, 105, 115, 116, 59, 120, 112, 115, 114, 0, 17, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 73, 110, 116, 101, 103, 101, 114, 18, -30, -96, -92, -9, -127, -121, 56, 2, 0, 1, 73, 0, 5, 118, 97, 108, 117, 101, 120, 114, 0, 16, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 78, 117, 109, 98, 101, 114, -122, -84, -107, 29, 11, -108, -32, -117, 2, 0, 0, 120, 112, 0, 0, 0, 20, 115, 113, 0, 126, 0, 5, 0, 0, 0, 1, 116, 0, 9, 120, 105, 97, 111, 122, 104, 97, 110, 103, 115, 114, 0, 19, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46, 65, 114, 114, 97, 121, 76, 105, 115, 116, 120, -127, -46, 29, -103, -57, 97, -99, 3, 0, 1, 73, 0, 4, 115, 105, 122, 101, 120, 112, 0, 0, 0, 1, 119, 4, 0, 0, 0, 1, 115, 113, 0, 126, 0, 5, 0, 0, 3, -13, 120]
        Player{id=1, age=20, name='xiaozhang', skills=[1011]}
        * */

    }
}

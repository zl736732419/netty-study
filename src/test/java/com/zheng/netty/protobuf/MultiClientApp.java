package com.zheng.netty.protobuf;

import com.zheng.netty.v5.MultiClient;

import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * @Author zhenglian
 * @Date 2019/4/14
 */
public class MultiClientApp {
    public static void main(String[] args) {
        MultiClient client = new MultiClient("192.168.3.12", 8000, 5);
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        while (true) {
            String line = scanner.nextLine();
            client.nextChannel().writeAndFlush(line);
        }
    }
}

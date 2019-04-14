package com.zheng.netty.nio.nettythread;

import java.util.Arrays;
import java.util.concurrent.Executors;

/**
 * 服务器启动入口
 * @Author zhenglian
 * @Date 2019/4/10
 */
public class ServerStartup {
    public static void main(String[] args) {
        int bossNum = 2;
        int workerNum = Runtime.getRuntime().availableProcessors() * 2;
        NioRunnablePool pool = new NioRunnablePool(
                Executors.newCachedThreadPool(),
                bossNum,
                Executors.newCachedThreadPool(),
                workerNum
        );
        
        ServerBootstrap bootstrap = new ServerBootstrap(pool);
        Integer[] ports = new Integer[] {8000, 8001};
        bootstrap.bind(ports);
        
        System.out.println("server start listening on " + Arrays.toString(ports));
    }
}

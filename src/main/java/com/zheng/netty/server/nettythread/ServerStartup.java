package com.zheng.netty.server.nettythread;

import java.util.concurrent.Executors;

/**
 * @Author zhenglian
 * @Date 2019/4/10
 */
public class ServerStartup {
    public static void main(String[] args) {
        NioRunnablePool pool = new NioRunnablePool(Executors.newFixedThreadPool(1),
                Executors.newCachedThreadPool());
        
        ServerBootstrap bootstrap = new ServerBootstrap(pool);
        
        int port = 8000;
        bootstrap.bind(port);
        
        System.out.println("server start listening on " + 8000);
    }
}

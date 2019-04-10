package com.zheng.netty.server.nettythread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * @Author zhenglian
 * @Date 2019/4/10
 */
public class ServerBootstrap {
    private NioRunnablePool pool;
    public ServerBootstrap(NioRunnablePool pool) {
        this.pool = pool;
    }
    
    public void bind(Integer port) {
        try {
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.bind(new InetSocketAddress(port));
            Boss boss = pool.nextBoss();
            boss.registerTask(serverChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

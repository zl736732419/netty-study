package com.zheng.netty.nio.nettythread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * 负责端口绑定，分发监听端口
 *
 * @Author zhenglian
 * @Date 2019/4/10
 */
public class ServerBootstrap {
    private NioRunnablePool pool;

    public ServerBootstrap(NioRunnablePool pool) {
        this.pool = pool;
    }

    public void bind(Integer... ports) {
        try {
            for (Integer port : ports) {
                ServerSocketChannel serverChannel = ServerSocketChannel.open();
                serverChannel.configureBlocking(false);
                serverChannel.bind(new InetSocketAddress(port));
                Boss boss = pool.nextBoss();
                boss.registerTask(serverChannel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

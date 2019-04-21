package com.zheng.netty.game.server;

import com.zheng.netty.game.codc.RequestDecoder;
import com.zheng.netty.game.codc.ResponseEncoder;
import com.zheng.netty.game.server.handler.ServerHandler;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 基于netty 3.10.6.Final
 * @Author zhenglian
 * @Date 2019/4/7
 */
public class GameServer {
    public static void main(String[] args) {
        // 服务类
        ServerBootstrap bootstrap = new ServerBootstrap();

        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();
        // 设置nio连接工厂
        bootstrap.setFactory(new NioServerSocketChannelFactory(boss, worker));
        // 设置管道工厂
        bootstrap.setPipelineFactory(() -> {
            ChannelPipeline pipeline = Channels.pipeline();
            pipeline.addLast("decoder", new RequestDecoder());
            pipeline.addLast("encoder", new ResponseEncoder());
            pipeline.addLast("server", new ServerHandler());
            return pipeline;
        });
        // 监听端口
        bootstrap.bind(new InetSocketAddress(8000));
        System.out.println("服务器已启动，正在监听8000端口");
    }
    
}

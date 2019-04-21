package com.zheng.netty.game.client;

import com.zheng.netty.game.client.handler.ClientHandler;
import com.zheng.netty.game.codc.RequestEncoder;
import com.zheng.netty.game.codc.ResponseDecoder;
import com.zheng.netty.game.model.Request;
import com.zheng.netty.game.module.fuben.request.FightRequest;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.SocketChannel;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 基于netty 3.10.6.Final
 * @Author zhenglian
 * @Date 2019/4/7
 */
public class GameClient {
    
    public static void main(String[] args) {
        // 设置服务类
        ClientBootstrap bootstrap = new ClientBootstrap();
        // 设置连接工厂
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();
        bootstrap.setFactory(new NioClientSocketChannelFactory(boss, worker));
        // 设置管道工厂
        bootstrap.setPipelineFactory(() -> {
            ChannelPipeline pipeline = Channels.pipeline();
            pipeline.addLast("decoder", new ResponseDecoder());
            pipeline.addLast("encoder", new RequestEncoder());
            pipeline.addLast("client", new ClientHandler());
            return pipeline;
        });
        // 连接服务器
        ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8000));
        SocketChannel channel = (SocketChannel) future.getChannel();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            int fubenId = Integer.parseInt(scanner.nextLine());
            int count = Integer.parseInt(scanner.nextLine());
            FightRequest fightRequest = new FightRequest();
            fightRequest.setFubenId(fubenId);
            fightRequest.setCount(count);
            Request request = new Request();
            request.setModule((short) 1);
            request.setCmd((short) 1);
            request.setData(fightRequest.getBytes());
            channel.write(request);
        }
    }
}

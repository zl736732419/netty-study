package com.zheng.netty.v3;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.SocketChannel;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 基于netty 3.10.6.Final
 * @Author zhenglian
 * @Date 2019/4/7
 */
public class HelloNettyClient {
    
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
            pipeline.addLast("decoder", new StringDecoder(StandardCharsets.UTF_8));
            pipeline.addLast("encoder", new StringEncoder(StandardCharsets.UTF_8));
            pipeline.addLast("hi", new ClientHandler());
            return pipeline;
        });
        // 连接服务器
        ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8000));
        SocketChannel channel = (SocketChannel) future.getChannel();
        Scanner scanner = new Scanner(System.in);
        String line;
        while (true) {
            System.out.println("请输入：");
            line = scanner.next();
            if (Objects.equals("bye", line)) {
                break;
            }
            channel.write(line);
        }
        channel.close();
    }

    private static class ClientHandler extends SimpleChannelHandler {
        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            System.out.println("messageReceived");
            String response = (String) e.getMessage();
            System.out.println("收到响应：" + response);
            super.messageReceived(ctx, e);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            System.out.println("exceptionCaught");
            super.exceptionCaught(ctx, e);
        }

        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            System.out.println("channelConnected");
            super.channelConnected(ctx, e);
        }

        @Override
        public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            System.out.println("channelDisconnected");
            super.channelDisconnected(ctx, e);
        }

        @Override
        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            System.out.println("channelClosed");
            super.channelClosed(ctx, e);
        }
    }
}

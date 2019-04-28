package com.zheng.netty.v4;

import com.zheng.netty.v4.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @Author zhenglian
 * @Date 2019/4/14
 */
public class NettyServer {
    public static void main(String[] args) {
        // 创建服务类
        ServerBootstrap bootstrap = new ServerBootstrap();

        // 创建线程池
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            bootstrap.group(boss, worker);

            // 创建socket工厂
            bootstrap.channel(NioServerSocketChannel.class);

            // 创建pipeline
            bootstrap.childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    // 添加心跳检测(这里是添加定时，用于触发空闲事件，事件处理由helloHandler处理)
                    ch.pipeline().addLast("idle", new IdleStateHandler(5, 0, 15));
                    ch.pipeline().addLast("decoder", new StringDecoder());
                    ch.pipeline().addLast("encoder", new StringEncoder());
                    ch.pipeline().addLast("hello", new ServerHandler());
                }
            });

            // 设置连接参数
            bootstrap.option(ChannelOption.SO_BACKLOG, 2048); // serverScoketChannel设置，连接缓冲区大小
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); // SocketChannel设置，维持连接的活跃
            bootstrap.childOption(ChannelOption.TCP_NODELAY, true); // SocketChannel设置，关闭消息延迟发送

            // 绑定端口
            ChannelFuture future = bootstrap.bind("192.168.3.12", 8000);
            System.out.println("sever start...");

            // 等待服务器关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}

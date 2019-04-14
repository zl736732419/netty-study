package com.zheng.netty.v5;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

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
                    ch.pipeline().addLast("decoder", new StringDecoder());
                    ch.pipeline().addLast("encoder", new StringEncoder());
                    ch.pipeline().addLast("hello", new HelloHandler());
                }
            });

            // 设置连接参数
            bootstrap.option(ChannelOption.SO_BACKLOG, 2048); // serverScoketChannel设置，连接缓冲区大小
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); // SocketChannel设置，维持连接的活跃
            bootstrap.childOption(ChannelOption.TCP_NODELAY, true); // SocketChannel设置，关闭消息延迟发送

            // 绑定端口
            ChannelFuture future = bootstrap.bind(8000);
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

    private static class HelloHandler extends SimpleChannelInboundHandler<String> {
        @Override
        protected void messageReceived(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
            System.out.println("收到消息:" + msg);
            channelHandlerContext.channel().writeAndFlush("hi");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            System.out.println("channelActive");
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            System.out.println("channelInactive");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
            System.out.println("exceptionCaught");
        }
    }
}

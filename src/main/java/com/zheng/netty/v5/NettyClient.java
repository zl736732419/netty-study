package com.zheng.netty.v5;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Scanner;

/**
 * @Author zhenglian
 * @Date 2019/4/14
 */
public class NettyClient {
    public static void main(String[] args) {
        // 服务类
        Bootstrap bootstrap = new Bootstrap();
        
        // 设置线程池
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            bootstrap.group(worker);
            
            // 设置连接
            bootstrap.channel(NioSocketChannel.class);
            
            // 设置管道
            bootstrap.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast("decoder", new StringDecoder());
                    ch.pipeline().addLast("encoder", new StringEncoder());
                    ch.pipeline().addLast("hi", new HiHandler());
                }
            });
            ChannelFuture future = bootstrap.connect("127.0.0.1", 8000);
            System.out.println("client start...");
            Scanner scanner = new Scanner(new InputStreamReader(System.in));
            while (true) {
                String line = scanner.nextLine();
                if (Objects .equals("bye", line)) {
                   break; 
                }
                future.channel().writeAndFlush(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
          worker.shutdownGracefully();  
        }
    }
    
    private static class HiHandler extends SimpleChannelInboundHandler<String> {
        @Override
        protected void messageReceived(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
            System.out.println("response: " + msg);
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
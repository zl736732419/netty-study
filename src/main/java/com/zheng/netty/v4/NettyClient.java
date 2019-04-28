package com.zheng.netty.v4;

import com.zheng.netty.v4.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

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
                    // 添加心跳,每隔3s发送一次心跳数据包
                    ch.pipeline().addLast("heart", new IdleStateHandler(0, 3, 0));
                    ch.pipeline().addLast("decoder", new StringDecoder());
                    ch.pipeline().addLast("encoder", new StringEncoder());
                    ch.pipeline().addLast("hi", new ClientHandler());
                }
            });
            ChannelFuture future = bootstrap.connect("192.168.3.12", 8000);
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
}

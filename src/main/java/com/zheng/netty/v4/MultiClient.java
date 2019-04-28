package com.zheng.netty.v4;


import com.zheng.netty.v4.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 单客户端多连接
 *
 * @Author zhenglian
 * @Date 2019/4/14
 */
public class MultiClient {
    private Bootstrap bootstrap;
    private List<Channel> channels;
    private String host;
    private Integer port;

    private AtomicInteger index = new AtomicInteger(0);

    public MultiClient(String host, Integer port, int count) {
        this.host = host;
        this.port = port;
        init(count);
    }

    private void init(int count) {
        channels = new ArrayList<>();
        // 服务类
        bootstrap = new Bootstrap();

        // 设置worker线程池
        NioEventLoopGroup worker = new NioEventLoopGroup();
        bootstrap.group(worker);
        // 设置连接
        bootstrap.channel(NioSocketChannel.class);
        // 设置pipeline
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) {
                ch.pipeline().addLast("decoder", new StringDecoder());
                ch.pipeline().addLast("encoder", new StringEncoder());
                ch.pipeline().addLast("hi", new ClientHandler());
            }
        });

        ChannelFuture future;
        for (int i = 0; i < count; i++) {
            future = bootstrap.connect(host, port);
            channels.add(future.channel());
        }
        System.out.println("multi client start");
    }

    public Channel nextChannel() {
        return getFirstActiveChannel(0);
    }

    private Channel getFirstActiveChannel(int count) {
        Channel channel = channels.get(index.getAndIncrement() % channels.size());
        if (!channel.isActive()) {
            reconnect(channel);
            if (count > channels.size()) {
                throw new RuntimeException("no active channel!");
            }
            return getFirstActiveChannel(count + 1);
        }
        return channel;
    }

    private void reconnect(Channel channel) {
        synchronized (channel) {
            int index = channels.indexOf(channel);
            if (index == -1) { // channel已经被移除
                return;
            }
            if (channels.get(index).isActive()) { // 已经重新连接
                return;
            }
            ChannelFuture future = bootstrap.connect(host, port);
            channels.set(index, future.channel());
        }

    }
}

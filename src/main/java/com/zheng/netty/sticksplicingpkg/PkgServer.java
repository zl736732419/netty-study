package com.zheng.netty.sticksplicingpkg;


import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author zhenglian
 * @Date 2019/4/23
 */
public class PkgServer {
    public static void main(String[] args) {
        // 服务类
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 线程池
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();
        // 设置连接工厂
        bootstrap.setFactory(new NioServerSocketChannelFactory(boss, worker));
        // 设置pipeline工厂
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                // 解决粘包和分包
                pipeline.addLast("stickSplicePkgHandler", new StickSplicePkgHandler());
//                pipeline.addLast("stringDecoder", new StringDecoder());
                pipeline.addLast("serverHandler", new StickSpliceServerHandler());
                return pipeline;
            }
        });

        //绑定端口
        bootstrap.bind(new InetSocketAddress(8000));
        System.out.println("server listen on 8000.");
    }

    private static class StickSpliceServerHandler extends SimpleChannelHandler {
        private int count;

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            String msg = (String) e.getMessage();
            System.out.println(msg + ":" + (++count));
        }
    }

    private static class StickSplicePkgHandler extends FrameDecoder {
        @Override
        protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
            // 定义发送的消息是4字节长度+string数据
            if (buffer.readableBytes() < 4) {
                return null; // 继续等待数据到来
            }
            // 标记当前准备读取数据的位置
            buffer.markReaderIndex();
            // 读取数据包长度
            int length = buffer.readInt();
            if (buffer.readableBytes() < length) {
                // 数据不完整，等待
                buffer.resetReaderIndex();
                return null;
            }
            byte[] bytes = new byte[length];
            buffer.readBytes(bytes);
            return new String(bytes, 0, length, StandardCharsets.UTF_8);
        }
    }
}

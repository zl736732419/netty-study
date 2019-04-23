package com.zheng.netty.sticksplicingpkg;


import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author zhenglian
 * @Date 2019/4/23
 */
public class PkgClient {
    public static void main(String[] args) throws Exception {
        // 创建服务类
        ClientBootstrap bootstrap = new ClientBootstrap();
        // 线程池
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();
        // 设置factory
        bootstrap.setFactory(new NioClientSocketChannelFactory(boss, worker));
        // 设置pipeline工厂
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                // 客户端的pipeline handler处理顺序与添加顺序相反
                pipeline.addLast("stickSpliceClientHandler", new StickSpliceClientHandler());
                pipeline.addLast("stringEncoder", new StringEncoder());
                return pipeline;
            }
        });
        // 连接服务器
        ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8000));
        Channel channel = future.getChannel();
        for (int i = 0; i < 1000; i++) {
            channel.write("hello");
        }
    }
    
    private static class StickSpliceClientHandler extends OneToOneEncoder {
        @Override
        protected Object encode(ChannelHandlerContext ctx, Channel channel, Object obj) throws Exception {
            ChannelBuffer buffer = (ChannelBuffer) obj;
            int length = buffer.readableBytes();
            ByteBuffer byteBuffer = ByteBuffer.allocate(4 + length);
            byteBuffer.putInt(length);
            byte[] data = new byte[length];
            buffer.readBytes(data);
            byteBuffer.put(data);
            ChannelBuffer out = ChannelBuffers.wrappedBuffer(byteBuffer.array());
            return out;
        }
    }
}

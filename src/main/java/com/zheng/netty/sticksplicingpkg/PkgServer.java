package com.zheng.netty.sticksplicingpkg;


import com.zheng.netty.game.constant.Constants;
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

    /**
     * 稳定安全能防止socket字节流攻击的解码器
     * +——-—----——+——-----——+——-----——+
     * |   包头    |   长度   |   数据   |
     * +——-—----——+——-----——+——-----——+
     * 包头：4bytes,标识数据包
     * 长度：4bytes，标识数据长度
     * 数据：真实传递的数据
     */
    private static class StickSplicePkgHandler extends FrameDecoder {
        // 包头 + 长度
        private static final int BASE_LENGTH = 4 + 4;
        // 设定最大数据包长度
        private static final int MAX_LENGTH = 2048;
        
        @Override
        protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
            // 数据包不完整，等待
            if (buffer.readableBytes() < BASE_LENGTH) {
                return null;
            }
            // 数据包过大，直接跳过这批数据
            if (buffer.readableBytes() > MAX_LENGTH) {
                buffer.skipBytes(buffer.readableBytes());
                return null;
            }
            int beginIndex; // 记录下一个包头的有效数据位置
            // 数据包清除可能会导致数据不完整，无法定位正确的包头位置，所以这里需要遍历直到定位到包头为止
            while (true) {
                buffer.markReaderIndex();
                beginIndex = buffer.readerIndex();
                // 成功定位到包头，开始往后读取数据
                if (buffer.readInt() == Constants.FLAG) {
                    break;
                }
                buffer.resetReaderIndex();
                // 如果没有找到，每次往前移动1byte，主要是防止读取过多字节会直接跳过下一个有效的包头位置
                buffer.readByte();
                // 字节读取之后可能会导致数据又不够的情况
                if (buffer.readableBytes() < BASE_LENGTH) {
                    return null;
                }
            }
            // 读取数据包长度
            int length = buffer.readInt();
            if (buffer.readableBytes() < length) {
                // 数据不完整，等待
                buffer.readerIndex(beginIndex);
                return null;
            }
            byte[] bytes = new byte[length];
            buffer.readBytes(bytes);
            return new String(bytes, 0, length, StandardCharsets.UTF_8);
        }
    }
}

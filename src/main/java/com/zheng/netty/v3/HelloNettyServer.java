package com.zheng.netty.v3;

/**
 * 基于netty 3.10.6.Final
 * @Author zhenglian
 * @Date 2019/4/7
 */
public class HelloNettyServer {
//    public static void main(String[] args) {
//        // 服务类
//        ServerBootstrap bootstrap = new ServerBootstrap();
//
//        ExecutorService boss = Executors.newCachedThreadPool();
//        ExecutorService worker = Executors.newCachedThreadPool();
//        // 设置nio连接工厂
//        bootstrap.setFactory(new NioServerSocketChannelFactory(boss, worker));
//        // 设置管道工厂
//        bootstrap.setPipelineFactory(() -> {
//            ChannelPipeline pipeline = Channels.pipeline();
//            pipeline.addLast("decoder", new StringDecoder(StandardCharsets.UTF_8));
//            pipeline.addLast("encoder", new StringEncoder());
//            pipeline.addLast("hello", new HelloHandler());
//            return pipeline;
//        });
//        // 监听端口
//        bootstrap.bind(new InetSocketAddress(8000));
//        System.out.println("服务器已启动，正在监听8000端口");
//    }
//
//    private static class HelloHandler extends SimpleChannelHandler {
//        /**
//         * 消息处理
//         * @param ctx
//         * @param e
//         * @throws Exception
//         */
//        @Override
//        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
//            System.out.println("messageReceived");
//            String msg = (String) e.getMessage();
//            System.out.println("消息接收：" + msg);
////            int a = 1/0; // 异常由exceptionCaught捕获处理
//
//            // 返回消息：
//            SocketChannel channel = (SocketChannel) ctx.getChannel();
////            ChannelBuffer outBuffer = ChannelBuffers.copiedBuffer("hi".getBytes());
////            channel.write(outBuffer);
//            channel.write("hi");
//
//            super.messageReceived(ctx, e);
//        }
//
//        /**
//         * 异常处理，messageReceived发生异常时会调用
//         * @param ctx
//         * @param e
//         * @throws Exception
//         */
//        @Override
//        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
//            System.out.println("exceptionCaught");
//            System.out.println(e.getCause().fillInStackTrace());
//            super.exceptionCaught(ctx, e);
//        }
//
//        /**
//         * 连接建立
//         * @param ctx
//         * @param e
//         * @throws Exception
//         */
//        @Override
//        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
//            System.out.println("channelConnected");
//            super.channelConnected(ctx, e);
//        }
//
//        /**
//         * 只有连接建立才会进行断开
//         * @param ctx
//         * @param e
//         * @throws Exception
//         */
//        @Override
//        public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
//            System.out.println("channelDisconnected");
//            SocketAddress address = e.getChannel().getRemoteAddress();
//            System.out.println("客户端" + address.toString() + "断开连接");
//            super.channelDisconnected(ctx, e);
//        }
//
//        /**
//         * 连接关闭，不管连接是否建立，都会关闭
//         * @param ctx
//         * @param e
//         * @throws Exception
//         */
//        @Override
//        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
//            System.out.println("channelClosed");
//            super.channelClosed(ctx, e);
//        }
//    }
}

package com.zheng.netty.server;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * NIO编程
 * 单一线程负责监听客户端连接以及处理多个客户端请求
 * @Author zhenglian
 * @Date 2019/4/7
 */
public class NIOServer { 
    private Selector selector;
    
    public void initServer(int port) throws Exception {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        // 将通道对应的server socket绑定到指定端口上
        serverChannel.socket().bind(new InetSocketAddress(port));
        // 获得一个通道管理器
        this.selector = Selector.open();
        // 将通道管理器与通道绑定，并为通道绑定SelectionKey.OP_ACCEPT事件。
        // 当事件发生时，selector.select()会返回，否则会一直阻塞下去
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void listen() throws Exception {
       System.out.println("服务端启动成功");
        Iterator<SelectionKey> iterator;
        while (true) {
           // 当注册事件到达时，方法返回，否则一直阻塞下去
           selector.select();
           iterator = selector.selectedKeys().iterator();
           while (iterator.hasNext()) {
               SelectionKey key = iterator.next();
               // 删除已经选择的key,防止重复处理
               iterator.remove();
               handle(key);
           }
       }
    }

    private void handle(SelectionKey key) {
        try {
            if (key.isAcceptable()) { // OP_ACCEPT
                handleAccept(key);
            } else if (key.isReadable()) { // OP_READ
                handleRead(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleRead(SelectionKey key) throws Exception {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read = socketChannel.read(buffer);
        if (read > 0) {
            byte[] data = buffer.array();
            String msg = new String(data).trim();
            System.out.println("服务端收到消息：" + msg);
            // 响应消息
            ByteBuffer outBuffer = ByteBuffer.wrap("yeah".getBytes());
            socketChannel.write(outBuffer);
        } else { // 客户端关闭后会抛出异常，死循环
            System.out.println("客户端关闭");
            key.cancel();
        }
    }

    private void handleAccept(SelectionKey key) throws Exception {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverChannel.accept();
        socketChannel.configureBlocking(false);
        System.out.println("新客户端连接");
        // 在和客户端连接成功之后，为了能读取信息，需要注册读事件
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    public static void main(String[] args) throws Exception {
        NIOServer server = new NIOServer();
        server.initServer(8000);
        server.listen();
    }
}

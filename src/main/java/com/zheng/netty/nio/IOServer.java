package com.zheng.netty.nio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 传统socket编程
 * 多线程处理客户端请求
 * @Author zhenglian
 * @Date 2019/4/7
 */
public class IOServer {
    private static final Integer PORT = 8000;
    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(PORT);
        ExecutorService executorService = Executors.newCachedThreadPool();
        System.out.println("服务器已启动，监听" + PORT + "端口...");
        while (true) {
            Socket socket = ss.accept(); 
            System.out.println("收到客户端连接请求，当前客户端为" + socket.getRemoteSocketAddress().toString());
            // 收到一个请求就建立线程处理，每个线程只能处理一个请求
            executorService.execute(() -> handle(socket));
        }
    }

    private static void handle(Socket socket) {
        byte[] bytes = new byte[1024];
        try {
            InputStream input = socket.getInputStream();
            int read;
            String line;
            while (true) {
                read = input.read(bytes);
                if (read == -1) {
                    break;
                }
                line = new String(bytes, 0, read);
                if (Objects.equals(line, "bye")) {
                    break;
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(socket);
        }
    }

    private static void close(Socket socket) {
        if (null != socket) {
            try {
                socket.close();
                System.out.println("连接已关闭.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

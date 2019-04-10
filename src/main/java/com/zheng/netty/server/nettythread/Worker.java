package com.zheng.netty.server.nettythread;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 负责处理客户端请求
 *
 * @Author zhenglian
 * @Date 2019/4/10
 */
public class Worker implements Runnable {
    private Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<>();
    private AtomicBoolean wakeUp = new AtomicBoolean(false);
    private String name;
    private NioRunnablePool pool;
    private Selector selector;

    public Worker(String name, ExecutorService executorService, NioRunnablePool pool) {
        this.name = name;
        this.pool = pool;
        openSelector();
        executorService.execute(this);
    }

    private void openSelector() {
        try {
            this.selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                wakeUp.set(false); // 初始化
                select();
                handleTask();
                processChannel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void processChannel() throws Exception {
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        if (selectionKeys.isEmpty()) {
            return;
        }
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            SocketChannel socketChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = socketChannel.read(buffer);
            if (read >= 0) {
                byte[] data = buffer.array();
                String msg = new String(data).trim();
                System.out.println(name + "收到消息：" + msg);
                // 响应消息
                ByteBuffer outBuffer = ByteBuffer.wrap("yeah".getBytes());
                socketChannel.write(outBuffer);
            } else { // 客户端关闭后会抛出异常，死循环
                System.out.println("客户端["+socketChannel.getRemoteAddress()+"]断开连接");
                key.cancel();
            }
        }
    }

    private void handleTask() {
        while (true) {
            Runnable task = taskQueue.poll();
            if (null == task) {
                break;
            }
            System.out.println(name + " handle request.");
            task.run();
        }
    }

    private void select() {
        try {
            selector.select();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerTask(SocketChannel socketChannel) {
        Runnable task = () -> {
            try {
                socketChannel.register(selector, SelectionKey.OP_READ);
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            }
        };
        taskQueue.offer(task);
        if (wakeUp.compareAndSet(false, true)) {
            selector.wakeup();
        }
    }
}

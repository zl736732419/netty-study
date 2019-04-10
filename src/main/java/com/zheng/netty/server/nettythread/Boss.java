package com.zheng.netty.server.nettythread;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 负责监听客户端请求
 * @Author zhenglian
 * @Date 2019/4/9
 */
public class Boss implements Runnable {
    private Queue<Runnable> taskQueue = new ConcurrentLinkedQueue();
    private NioRunnablePool pool; 
    private Selector selector;
    private String name;
    private AtomicBoolean wakeUp = new AtomicBoolean(false);
    
    public Boss(String name, ExecutorService executorService, NioRunnablePool pool) {
        this.name = name;
        this.pool = pool;
        openSelector();
        executorService.execute(this);
    }

    @Override
    public void run() {
        try {
            while(true) {
                wakeUp.set(false); // 任务执行完成后，重置
                select();
                handleTask();
                processChannel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processChannel() throws Exception {
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        if (selectionKeys.isEmpty()) { // 还没有请求到来
            return;
        }
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        SelectionKey key;
        while (iterator.hasNext()) {
            key = iterator.next();
            iterator.remove();
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = serverChannel.accept();
            socketChannel.configureBlocking(false);
            // 交由worker负责处理
            Worker nextWorker = pool.nextWorker();
            nextWorker.registerTask(socketChannel);
            System.out.println("新客户端连接:" + socketChannel.getRemoteAddress());
        }
    }

    private void handleTask() {
        while(true) {
            Runnable task = taskQueue.poll();
            if (null == task) {
                break;
            }
            System.out.println(name + " handle task.");
            task.run();
        }
    }

    private void select() {
        try {
            // 阻塞，等待客户端连接
            selector.select();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openSelector() {
        try {
            this.selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void registerTask(ServerSocketChannel serverChannel) {
        Runnable task = () -> {
            try {
                serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            }
        };
        taskQueue.add(task);
        // 唤醒阻塞的select
        if (wakeUp.compareAndSet(false, true)) {
            selector.wakeup();
        }
    }
}

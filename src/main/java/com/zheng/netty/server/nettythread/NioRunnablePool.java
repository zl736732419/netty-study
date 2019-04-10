package com.zheng.netty.server.nettythread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 工作线程池
 * @Author zhenglian
 * @Date 2019/4/9
 */
public class NioRunnablePool {
    
    private Boss[] bosses;
    private Worker[] workers;
    private AtomicInteger bossIndex = new AtomicInteger(0);
    private AtomicInteger workerIndex = new AtomicInteger(0);
    
    public NioRunnablePool(ExecutorService boss, ExecutorService worker) {
        int bossNum = 1;
        int workerNum = Runtime.getRuntime().availableProcessors() * 2;
        initBoss(bossNum, boss);
        initWorker(workerNum, worker);
        System.out.println(bossNum + " boss start, " + workerNum + " workers start.");
    }

    private void initWorker(int num, ExecutorService worker) {
        workers = new Worker[num];
        for (int i = 0; i < num; i++) {
            workers[i] = new Worker("worker-thread-" + i, worker, this);
        }
    }

    private void initBoss(int num, ExecutorService boss) {
        bosses = new Boss[num];
        for (int i = 0; i < num; i++) {
            bosses[i] = new Boss("boss-thread-" + i, boss, this);
        }
    }

    public Boss nextBoss() {
        return bosses[Math.abs(bossIndex.getAndIncrement() % bosses.length)];
    }
    
    public Worker nextWorker() {
        return workers[Math.abs(workerIndex.getAndIncrement() % workers.length)];
    }
    
    
}

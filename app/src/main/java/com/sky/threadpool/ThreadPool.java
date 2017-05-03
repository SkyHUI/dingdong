package com.sky.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
 * 线程池为无限大，当执行第二个任务时第一个任务已经完成，会复用执行第一个任务的线程，而不用每次新建线程。
 * 
 * 单例模式
 * @author Snow
 *
 */
public class ThreadPool {

    /**
     * 私有化构造函数
     * 不允许直接创建
     */
    private ThreadPool(){
        
    }
    
    private static class Singleton{
        private static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    }
    
    /**
     * 只能通过此方法获取 线程池的单例对象
     * @return
     */
    public static ExecutorService getInstance(){
        return Singleton.cachedThreadPool;
    }
    
}

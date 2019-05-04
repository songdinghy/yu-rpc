package com.sd.rpc.client;

import com.sd.rpc.demo.HelloService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Martin.S
 * @Description
 * @date 2019/4/29 11:21
 */
public class RpcClient {
    public static void main(String[] args) {
        // 从zk获取服务地址

        // 封装协议信息

        HelloService helloService = RpcProxy.create(HelloService.class);
        long startTime = System.currentTimeMillis();
        final AtomicLong cnt = new AtomicLong(0);
        for (int i = 0; i <30000; i++) {
            new Thread(()->{
                while(true){
                    helloService.sayHello("玉");
                    cnt.incrementAndGet();
                }
            }).start();
        }

        new Thread(new Runnable() {
            private long last = 0;

            @Override
            public void run() {
                while (true) {
                    long count = cnt.get();
                    long tps = count - last;
                    System.out.println("last 1s invoke: "+ tps);
                    last = count;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }
            }
        }, "Print-tps-THREAD").start();
    }
}

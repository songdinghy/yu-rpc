package com.sd.rpc.client;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Martin.S
 * @Description
 * @date 2019/4/29 12:40
 */
public class RpcMsg<T> {
    private T t;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    public void setData(T t){
        try {
            lock.lock();
            this.t=t;
            condition.signal();
        } finally {
            lock.unlock();
        }
    }


    public T getData(){
        lock.lock();
        try {
            if(t==null){
                condition.await(1000L, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return t;
    }
}

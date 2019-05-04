package com.sd.rpc.client;

/**
 * @author Martin.S
 * @Description
 * @date 2019/4/29 11:22
 */

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * JDK动态代理类
 *
 *
 */
public class RpcProxy<T> implements InvocationHandler {

    private Class<T> interfaceClass;

    public RpcProxy(Class<T> interfaceClass) {
        this.interfaceClass=interfaceClass;
    }

    public static <T> T create(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new RpcProxy<T>(interfaceClass)
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)//invoke方法
            throws Throwable {
        before();
        Object ret = null;      // 设置方法的返回值
        // ret  = method.invoke(targetObject, args);       //invoke调用需要代理的方法
        // 发送IO请求
        NettyClient nettyClient = NettyClient.getInstance();
        RpcMsg rpcMsg = nettyClient.invoke(args[0].toString());
        ret = rpcMsg.getData();
        after();
        return ret;
    }

    private void before() {//方法执行前
       // System.out.println("方法执行前 !");
    }
    private void after() {//方法执行后
       // System.out.println("方法执行后");
    }
}
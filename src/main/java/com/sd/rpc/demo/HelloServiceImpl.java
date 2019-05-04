package com.sd.rpc.demo;

/**
 * @author Martin.S
 * @Description
 * @date 2019/4/29 11:24
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name){
        return "love:"+name;
    }
}

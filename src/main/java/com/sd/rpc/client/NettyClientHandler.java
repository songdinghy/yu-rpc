package com.sd.rpc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Title: NettyClientHandler
 * Description: 客户端业务逻辑实现
 * Version:1.0.0
 * @author Administrator
 * @date 2017-8-31
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<String> {
    private ConcurrentHashMap<String,RpcMsg> rpcMsgMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //System.out.println("客户端接受的消息: " + msg);
        String[] msgArr = msg.split(",");
       RpcMsg rpcMsg =  rpcMsgMap.get(msgArr[0]);
       if(rpcMsg !=null){
           rpcMsg.setData(msgArr[1]);
       }
        rpcMsgMap.remove(msgArr[0]);
        //ctx.close();
    }

    //
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("正在连接... ");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接关闭! ");
        super.channelInactive(ctx);
    }

    public ConcurrentHashMap<String, RpcMsg> getRpcMsgMap() {
        return rpcMsgMap;
    }

}
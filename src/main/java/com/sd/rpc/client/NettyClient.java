package com.sd.rpc.client;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.util.UUID;

/**
 *
 * Title: NettyClient
 * Description: Netty客户端
 * Version:1.0.0
 * @author Administrator
 * @date 2017-8-31
 */
public class NettyClient {

    public  String host = "127.0.0.1";  //ip地址
    public  int port = 6789;          //端口
    /// 通过nio方式来接收连接和处理连接
    private  EventLoopGroup group = new NioEventLoopGroup();
    private   Bootstrap b = new Bootstrap();
    private  Channel ch;
    private static  NettyClient nettyClient;

    public  static  synchronized  NettyClient getInstance(){
            if(nettyClient == null){
                nettyClient = new NettyClient();
            }
            return nettyClient;
    }
    /**
     * Netty创建全部都是实现自AbstractBootstrap。
     * 客户端的是Bootstrap，服务端的则是    ServerBootstrap。
     **/
    private NettyClient(){
        try {
            System.out.println("客户端成功启动...");
            b.option(ChannelOption.SO_KEEPALIVE,true);
            b.group(group);
            b.channel(NioSocketChannel.class);
            b.handler(new NettyClientFilter());
            // 连接服务端
            ChannelFuture f = b.connect(host, port).sync();
            ch = f.channel();
            //// 等待客户端链路关闭
            //ch.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } /*finally {
            group.shutdownGracefully();
        }*/
    }

    public RpcMsg invoke(String str){
        RpcMsg rpcMsg = new RpcMsg();
        String requestId = UUID.randomUUID().toString();
        StringBuffer sb = new StringBuffer(requestId);
        sb.append(",");
        sb.append(str);
        ch.writeAndFlush(sb.toString()+ "\r\n");
        //System.out.println("客户端发送数据:"+str);
        NettyClientHandler clientHandler = ch.pipeline().get(NettyClientHandler.class);
        clientHandler.getRpcMsgMap().put(requestId,rpcMsg);
        return rpcMsg;
    }
}
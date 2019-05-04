package com.sd.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *
 * Title: NettyServer
 * Description: Netty服务端
 * Version:1.0.0
 * @author Administrator
 * @date 2017-8-31
 */
public class RpcServer {
    private static final int port = 6789; //设置服务端端口
    //连接处理group
    private static  EventLoopGroup boss = new NioEventLoopGroup();
    //事件处理group
    private static   EventLoopGroup worker = new NioEventLoopGroup();
    private static  ServerBootstrap b = new ServerBootstrap();

    /**
     * Netty创建全部都是实现自AbstractBootstrap。
     * 客户端的是Bootstrap，服务端的则是    ServerBootstrap。
     **/
    public static void main(String[] args) throws InterruptedException {
        try {
            b.group(boss,worker);
            b.channel(NioServerSocketChannel.class);
            //通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
            b.option(ChannelOption.TCP_NODELAY, true);
            //保持长连接状态
            b.childOption(ChannelOption.SO_KEEPALIVE, true);

            b.childHandler(new ServerFilter()); //设置过滤器
            // 服务器绑定端口监听
            ChannelFuture f = b.bind(port).sync();
            System.out.println("服务端启动成功...");
            // 监听服务器关闭监听
            f.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully(); ////关闭EventLoopGroup，释放掉所有资源包括创建的线程
            worker.shutdownGracefully(); ////关闭EventLoopGroup，释放掉所有资源包括创建的线程
        }
    }
}
package com.honey.netty.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author ：Brayden
 * @date ：Created in 2021/1/21 13:38
 * @description：
 * @modified By：
 * @version:
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        /**
         * 创建bossGroup 跟workGroup
         *
         *1.创建两个线程组 bossGroup 跟workGroup
         *2.bossGroup处理客户端连接请求，workGroup处理业务请求
         *3.两个都是无限循环
         *4.bossGroup跟workGroup含有的子线程数（NioEvenLoop）默认=cpu核数*2
         */
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            //创建服务端启动对象，配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //使用链式编程进行设置
            serverBootstrap.group(bossGroup)//设置线程组
                    .channel(NioServerSocketChannel.class)//使用NioServerSocketChannel作为服务器通道的实现
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列得到的连接数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            System.out.println("客户socketchannel hashcode=" + socketChannel.hashCode());
                            //可以使用一个集合管理 SocketChannel， 再推送消息时，
                            // 可以将业务加入到各个channel 对应的 NIOEventLoop 的 taskQueue 或者 scheduleTaskQueue
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });// 给我们的workerGroup 的 EventLoop 对应的管道设置处理器
            System.out.println("...........服务器is ready....");

            //绑定一个端口并且同步，生成一个ChannelFuture对象
            //启动服务器(并绑定端口)
            ChannelFuture cf = serverBootstrap.bind(8989).sync();

            //给cf注册监听器，监听我们关心的事

            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (cf.isSuccess()) {
                        System.out.println("监听8989成功");
                    } else {
                        System.out.println("监听8989失败");
                    }
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}

package com.honey.netty.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author ：Brayden
 * @date ：Created in 2021/1/18 13:57
 * @description：群聊服务端代码编写
 * @modified By：
 * @version:
 */
public class GroupChatServer {

    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    private Integer port = 8989;


    public GroupChatServer() {
        try {
            //得到选择器
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            //绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            //设置非阻塞模式
            serverSocketChannel.configureBlocking(false);
            //将serverSocketChannel注册到选择器
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void Listen() {
        //循环处理
        while (true) {
            try {
                int select = selector.select();
                if (select > 0) {//有事件处理
                    //遍历得到所有的selectionKey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        //监听到accept事件
                        if (selectionKey.isAcceptable()) {
                            //从selector中获取到通道
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            //设置通道的模式为非阻塞
                            socketChannel.configureBlocking(false);
                            //将通道注册到选择器上
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress() + "他妈的上线了");
                        }
                        //监听到read事件
                        if (selectionKey.isReadable()) {
                            //处理读事件
                            readData(selectionKey);
                        }
                        //当前的key删除，防止重复处理
                        iterator.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readData(SelectionKey selectionKey) {
        SocketChannel channel = null;
        try {
            //从选择器中获取通道
            channel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(2014);
            //通道中读取数据到缓存
            int read = channel.read(byteBuffer);
            if (read > 0) {
                //把缓存区数据处理成消息
                String msg = new String(byteBuffer.array());
                //输出改消息
                System.out.println("from他妈的客户端消息：" + msg);
                //转发消息给其他链接的客户端
                tranInfoClient(msg, channel);

            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                System.out.println(channel.getRemoteAddress() + "他奶奶的离线了");
                selectionKey.cancel();
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void tranInfoClient(String msg, SocketChannel channel) {
        //得到所有注册到selector上的SocketChannel
        Set<SelectionKey> keys = selector.keys();
        try {
            for (SelectionKey selectionKey : keys) {
                Channel targetChannel = selectionKey.channel();
                //排除自己，给其他客户端转发消息
                if (targetChannel instanceof SocketChannel && targetChannel != channel) {
                    //转型
                    SocketChannel dest = (SocketChannel) targetChannel;
                    ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                    dest.write(byteBuffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.Listen();
    }
}

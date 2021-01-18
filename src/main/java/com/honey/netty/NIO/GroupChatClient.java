package com.honey.netty.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author ：Brayden
 * @date ：Created in 2021/1/18 14:51
 * @description：群聊客户端代码
 * @modified By：
 * @version:
 */
public class GroupChatClient {

    private String host = "127.0.0.1";

    private Integer port = 8989;

    private SocketChannel socketChannel;

    private Selector selector;

    private String username;


    public GroupChatClient() {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            //设置非阻塞
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            username = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(username + " is ok...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //向服务器发送消息
    public void sendInfo(String info) {
        info = username + " 说：" + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //读取服务端回复的消息
    public void readInfo(){
        try {
            int select = selector.select();
            if(select>0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    if(selectionKey.isReadable()){
                        //得到相关的通道
                        SocketChannel sc = (SocketChannel) selectionKey.channel();
                        //得到一个 Buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        //读取
                        sc.read(buffer);
                        //把读到的缓冲区的数据转成字符串
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }
                }
                iterator.remove(); //删除当前的 selectionKey,防止重复操作
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GroupChatClient groupChatClient = new GroupChatClient();
        //启动一个线程,每个 3 秒，读取从服务器发送数据
        new Thread(){
            public void run() {
                while (true) {
                    groupChatClient.readInfo();
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        //发送数据给服务器端
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            groupChatClient.sendInfo(s);
        }
    }

}

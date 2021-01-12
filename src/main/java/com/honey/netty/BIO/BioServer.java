package com.honey.netty.BIO;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ：Brayden
 * @date ：Created in 2021/1/12 10:46
 * @description：
 * @modified By：
 * @version: BIO测试
 */
public class BioServer {

    public static void main(String[] args) throws IOException {

        //服务器监听8989端口
        ServerSocket serverSocket = new ServerSocket(8989);
        System.out.println("位置1线程"+Thread.currentThread().getName());
        //适用于负载较轻的场景，执行异步短期的任务，很快可以结束，避免cpu过度切换（原理采用无限扩大的线程池）
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (true) {
            System.out.println("没有客户端连接时，我在这里阻塞了");
            Socket accept = serverSocket.accept();
            System.out.println("客户端连接");
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("位置2线程"+Thread.currentThread().getName());
                    handler(accept);
                }
            });
        }
    }


    public static void handler(Socket socket) {
        try {
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            while (true) {
                System.out.println("位置3线程"+Thread.currentThread().getName());
                System.out.println("客户端没有发数据时，我在这里阻塞了");
                int read = inputStream.read(bytes);
                if (read != -1) {
                    String s = new String(bytes, 0, read);
                    System.out.println(s);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭和client的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


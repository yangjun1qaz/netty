package com.honey.netty.NIO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Description：
 * @Date: Created in 2021/1/13 23:37
 * @Author Brayden
 * @Version 1.0
 */
public class FileChannel01 {
    public static void main(String[] args) throws IOException {

        String str="love brayden";

        FileOutputStream fileOutputStream = new FileOutputStream("D:\\brayden.txt");
        //从流中获取管道
        FileChannel channel = fileOutputStream.getChannel();
        //创建缓存buf
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        //缓存中放入数据
        allocate.put(str.getBytes());
        //数据写完了，反转，进行读数据
        allocate.flip();
        //缓存中数据写入管道
        channel.write(allocate);

        fileOutputStream.close();

    }
}


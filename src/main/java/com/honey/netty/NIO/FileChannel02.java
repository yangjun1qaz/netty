package com.honey.netty.NIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author ：Brayden
 * @date ：Created in 2021/1/14 17:09
 * @description：将文件中的数据读取出来，输出到控制台
 * @modified By：
 * @version:
 */
public class FileChannel02 {
    public static void main(String[] args) throws IOException {
        File file = new File("d:\\brayden.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        //从输入流中获取通道
        FileChannel channel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        //管道中读取数据到缓存区
        channel.read(byteBuffer);

        System.out.println(new String(byteBuffer.array()));

        fileInputStream.close();


    }
}

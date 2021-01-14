package com.honey.netty.NIO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author ：Brayden
 * @date ：Created in 2021/1/14 18:05
 * @description：把一份文件从一个位置copy到另一个位置
 * @modified By：
 * @version:
 */
public class FileChannel03 {

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("brayden.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("braydenyang.txt");

        //获取通道
        FileChannel channel01 = fileInputStream.getChannel();
        FileChannel channel02 = fileOutputStream.getChannel();

        //创建一个缓存（块）
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true) {
            //此处如果不复位，导致while死循环
            byteBuffer.clear();
            int read = channel01.read(byteBuffer);
            if (read == -1) {
                break;
            }
            //切换读写方式
            byteBuffer.flip();
            channel02.write(byteBuffer);
        }
        fileInputStream.close();
        fileOutputStream.close();
    }
}

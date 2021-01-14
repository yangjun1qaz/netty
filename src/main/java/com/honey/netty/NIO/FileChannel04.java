package com.honey.netty.NIO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author ：Brayden
 * @date ：Created in 2021/1/14 18:05
 * @description：将一个文件从一个位置copy到另外一个位置
 * @modified By：
 * @version:
 */
public class FileChannel04 {

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("D:\\brayden.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\braydenyang.jpg");

        FileChannel sourceCh = fileInputStream.getChannel();
        FileChannel targetCh = fileOutputStream.getChannel();

        targetCh.transferFrom(sourceCh, 0, sourceCh.size());

        sourceCh.close();
        targetCh.close();
        fileInputStream.close();
        fileOutputStream.close();


    }
}

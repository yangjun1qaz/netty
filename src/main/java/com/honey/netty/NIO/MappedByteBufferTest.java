package com.honey.netty.NIO;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Description： NIO 还提供了 MappedByteBuffer，可以让文件直接在内存（堆外的内存）中进行修改，
 * 而如何同步到文件由 NIO 来完成
 * @Date: Created in 2021/1/14 22:34
 * @Author Brayden
 * @Version 1.0
 */
public class MappedByteBufferTest {

    public static void main(String[] args) throws Exception {
        RandomAccessFile rw = new RandomAccessFile("brayden.txt", "rw");

        FileChannel channel = rw.getChannel();

        /**
         * 参数 1:FileChannel.MapMode.READ_WRITE 使用的读写模式
         * 参数 2：0：可以直接修改的起始位置
         * 参数 3:5: 是映射到内存的大小（不是索引位置），即将 1.txt 的多少个字节映射到内存
         * 可以直接修改的范围就是 0-5
         * 实际类型 DirectByteBuffer
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0,(byte) '9');
        mappedByteBuffer.put(1,(byte) '8');

        rw.close();

    }

}

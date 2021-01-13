package com.honey.netty.NIO;

import java.nio.ByteBuffer;

/**
 * @Description：
 * @Date: Created in 2021/1/13 22:22
 * @Author Brayden
 * @Version 1.0
 */
public class ByteBufferTest {

    public static void main(String[] args) {

        //创建一个buffer
        ByteBuffer allocate = ByteBuffer.allocate(1024);


        for (int i = 0; i < 2; i++) {
            allocate.put((byte) i);
        }

        allocate.flip();


        while (allocate.hasRemaining()) {
            System.out.println((int)allocate.get());
        }
    }
}

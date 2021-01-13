package com.honey.netty.NIO;

import java.nio.IntBuffer;

/**
 * @Description：
 * @Date: Created in 2021/1/13 22:22
 * @Author Brayden
 * @Version 1.0
 */
public class BufferTest {

    public static void main(String[] args) {

        //创建一个buffer
        IntBuffer intBuffer = IntBuffer.allocate(5);

        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i);
        }

        intBuffer.flip();


        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}

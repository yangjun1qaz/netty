package com.honey.netty.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @Description：
 * @Date: Created in 2021/1/16 17:24
 * @Author Brayden
 * @Version 1.0
 */
public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到socket，并启动
        serverSocketChannel.bind(inetSocketAddress);


        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);


        SocketChannel accept = serverSocketChannel.accept();

        int maxLength = 8;//假定从客户端读取的最大字节数
        while (true) {
            int byteRead = 0;
            while (byteRead < maxLength) {
                long read = accept.read(byteBuffers);
                byteRead += 1;
                System.out.println("byteRead = " + byteRead);
                Arrays.asList(byteBuffers).stream().map(byteBuffer -> "position=" + byteBuffer.position() + "limit=" + byteBuffer.limit()).forEach(System.out::println);
            }

            //缓存区进行反转
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());

            int byteWrite = 0;
            while (byteWrite < maxLength) {
                long write = accept.write(byteBuffers);
                byteWrite += 1;
            }

            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.clear());
            System.out.println("byteRead=" + byteRead + ",byteWrite=" + byteWrite + ",maxLength=" + maxLength);
        }
    }
}

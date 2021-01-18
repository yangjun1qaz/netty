package com.honey.netty.NIO;

/**
 * @author ：Brayden
 * @date ：Created in 2021/1/15 16:42
 * @description：
 * @modified By：
 * @version:
 */
public class Test {
    public static void main(String[] args) {
        String key="syn|channel|%s|*";
        String format = String.format(key, "1");
        System.out.println(format);
    }
}

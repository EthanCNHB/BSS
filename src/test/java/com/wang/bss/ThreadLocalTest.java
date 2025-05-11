package com.wang.bss;

import org.junit.jupiter.api.Test;

public class ThreadLocalTest {

    @Test
    public void testThreadLocalSetAndGet() {
        //提供一个ThreadLocal对象
        ThreadLocal tl = new ThreadLocal();

        //开启两个线程
        new Thread(() -> {
            tl.set("甲亢哥");
            System.out.println(Thread.currentThread().getName() + ":" + tl.get());
            System.out.println(Thread.currentThread().getName() + ":" + tl.get());
            System.out.println(Thread.currentThread().getName() + ":" + tl.get());
        },"黑色").start();
        new Thread(() -> {
            tl.set("田一鸣");
            System.out.println(Thread.currentThread().getName() + ":" + tl.get());
            System.out.println(Thread.currentThread().getName() + ":" + tl.get());
            System.out.println(Thread.currentThread().getName() + ":" + tl.get());
        }, "黄色").start();

    }
}

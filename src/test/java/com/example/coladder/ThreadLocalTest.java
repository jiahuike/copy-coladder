package com.example.coladder;

import org.junit.jupiter.api.Test;

public class ThreadLocalTest {

    @Test
    public void test(){
        ThreadLocal tl = new ThreadLocal<>();

        new Thread(()->{
            tl.set("xiaoyan");
            System.out.println(Thread.currentThread().getName() + " " + tl.get());
            System.out.println(Thread.currentThread().getName() + " " + tl.get());
            System.out.println(Thread.currentThread().getName() + " " + tl.get());
        },"lan").start();


        new Thread(()->{
            tl.set("yaoyan");
            System.out.println(Thread.currentThread().getName() + " " + tl.get());
            System.out.println(Thread.currentThread().getName() + " " + tl.get());
            System.out.println(Thread.currentThread().getName() + " " + tl.get());
        },"lve").start();

    }
}

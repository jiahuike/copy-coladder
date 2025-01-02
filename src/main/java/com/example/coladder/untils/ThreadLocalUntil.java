package com.example.coladder.untils;


public class ThreadLocalUntil {

    private static final ThreadLocal THREAD_LOCAL = new ThreadLocal();

    /**
     * 根据键值获取值
     * @return 相应的值
     * @param <T>
     */
    public static <T> T get() {
        return (T) THREAD_LOCAL.get();
    }

    /**
     * 存储键值对
     * @param obj 键值对
     */
    public static void set(Object obj) {
        THREAD_LOCAL.set(obj);
    }

    /**
     * 清除ThreadLocal，防止内存泄露
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }

}

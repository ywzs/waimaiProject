package com.ywzs.utils;


/**
 * ThreadLocal在线程上单独存储某一些值
 */
public class UserHolder {
    private static final ThreadLocal<Long> tl = new ThreadLocal<>();

    public static void save(Long id) {
        tl.set(id);
    }

    public static Long getId() {
        return tl.get();
    }

    public static void removeId() {
        tl.remove();
    }
}


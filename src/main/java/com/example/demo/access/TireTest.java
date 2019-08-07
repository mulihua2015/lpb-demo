package com.example.demo.access;

import java.lang.reflect.Method;

public class TireTest {

    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("com.example.demo.access.Tire");
        Object o = clazz.newInstance();
        Method method = clazz.getDeclaredMethod("setTireName", String.class);
        method.setAccessible(true);
        method.invoke(o,"张三");
        System.out.println(o);
    }
}

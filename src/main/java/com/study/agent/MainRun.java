package com.study.agent;

/**
 * Agent增强目标类
 */
public class MainRun {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            System.out.println("process result: " + hello("world"));
            Thread.sleep(5000);
        }
    }

    private static String hello(String name) {
        return "hello " + name;
    }
}

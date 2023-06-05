package com.study.agent;

/**
 * Agent增强目标类
 *
 */
public class MainRun
{
    public static void main(String[] args) {
        hello("world");
    }
    private static void hello(String name) {
        System.out.println("hello " + name);
    }
}

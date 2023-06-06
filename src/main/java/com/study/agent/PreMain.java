package com.study.agent;

/**
 * Agent,静态加载 增强目标类
 *
 * <p>
 * 测试premain时步骤
 * 1. 使用maven package对工程打包，在target目录下生成：agent-1.0-SNAPSHOT-jar-with-dependencies.jar
 * 2. 复制 agent-1.0-SNAPSHOT-jar-with-dependencies.jar 包的绝对路径：xxx/target/agent-1.0-SNAPSHOT-jar-with-dependencies.jar。
 * 3. 在MainRun目标类的启动参数里增加 vm options：-javaagent: xxx/target/agent-1.0-SNAPSHOT-jar-with-dependencies.jar。
 * 4. 运行MainRun目标类的main方法。
 * </p>
 */
public class PreMain {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            System.out.println("premain result: " + hello("world"));
            Thread.sleep(5000);
        }
    }

    private static String hello(String name) {
        return "hello " + name;
    }
}

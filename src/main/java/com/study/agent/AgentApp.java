package com.study.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * javaagent
 */
public class AgentApp {

    /**
     * 静态加载
     * 
     * @param agentOps
     * @param inst
     */
    public static void premain(String agentOps, Instrumentation inst) {
        System.out.println("==enter premain==");
        System.out.println(agentOps);
        inst.addTransformer(new AgentInstrumentation());
    }

    /**
     * 动态加载
     *
     * @param agentOps
     * @param inst
     */
    public static void agentmain(String agentOps, Instrumentation inst) {
        System.out.println(" ==enter agentmain:==");
    }

    /**
     * Java Instrumentation API
     * 允许在运行时（runtime）修改已经加载到Java虚拟机中的字节码。
     * Instrumentation API提供了一种实现AOP（面向切面编程）的方式，同时还可以用于代码注入、性能分析等任务。
     * 
     * Transformer通常需要继承ClassFileTransformer接口，重载其中的transform方法来实现对字节码的转换。
     */
    private static class AgentInstrumentation implements ClassFileTransformer {

        /**
         * 使用javassist对类增强
         */
        public byte[] transform(ClassLoader loader,
                String className,
                Class<?> classBeingRedefined,
                ProtectionDomain protectionDomain,
                byte[] classfileBuffer) throws IllegalClassFormatException {
            // 对指定类的方法实现增强
            if (className.contains("MainRun")) {
                try {
                    final String loadName = className.replaceAll("/", ".");
                    final CtClass ctClass = ClassPool.getDefault().get(loadName);
                    CtMethod ctMethod = ctClass.getDeclaredMethod("hello");
                    ctMethod.addLocalVariable("begin", CtClass.longType);
                    ctMethod.insertBefore("begin = System.nanoTime();");
                    ctMethod.insertAfter("System.out.println(System.nanoTime() - begin);");
                    ctMethod.insertAfter("System.out.println(\"hello world\");");
                    System.out.println(className);
                    return ctClass.toBytecode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return classfileBuffer;
        }
    }
}

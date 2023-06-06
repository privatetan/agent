package com.study.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
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
     * @param args
     * @param inst
     */
    public static void premain(String args, Instrumentation inst) {
        System.out.println("premain start");
        System.out.println(args);
        inst.addTransformer(new AgentInstrumentation());
    }

    /**
     * 动态加载
     *
     * @param args
     * @param inst
     */
    public static void agentmain(String args, Instrumentation inst) {
        System.out.println("agentmain start");
        inst.addTransformer(new AgentInstrumentation());
        Class[] allLoadedClasses = inst.getAllLoadedClasses();
        if (allLoadedClasses != null){
            for (Class<?> c: allLoadedClasses) {
                if (c.isInterface() ||c.isAnnotation() ||c.isArray() ||c.isEnum()){
                    continue;
                }
                if (c.getName().equals("com.study.agent.MainRun")) {
                    try {
                        System.out.println("retransformClasses start, class: " + c.getName());
                        /*
                         * retransformClasses()对JVM已经加载的类重新触发类加载。使用的就是上面注册的Transformer。
                         * retransformClasses()可以修改方法体，但是不能变更方法签名、增加和删除方法/类的成员属性
                         */
                        inst.retransformClasses(c);
                        System.out.println("retransformClasses end, class: " + c.getName());
                    } catch (UnmodifiableClassException e) {
                        System.out.println("retransformClasses error, class: " + c.getName() + ", ex:" + e);
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("agentmain end!");
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

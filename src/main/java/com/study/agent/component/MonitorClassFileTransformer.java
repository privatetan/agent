package com.study.agent.component;

import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * agent监控与诊断
 * <p>
 * 对指定方法执行时间的统计，并在控制台输出相关信息。
 */
public class MonitorClassFileTransformer implements ClassFileTransformer {
    private String classNameToMonitor = "com.study.agent.component.MonitorMain";
    private String methodNameToMonitor = "hello";


    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer)
            throws IllegalClassFormatException {
        if (className.equals(classNameToMonitor)) {
            System.out.println("Transforming class: " + className);
            ClassReader cr = new ClassReader(classfileBuffer);
            ClassWriter cw = new ClassWriter(cr, 0);
            ClassVisitor cv = new MonitorClassVisitor(cw, methodNameToMonitor);
            cr.accept(cv, 0);
            return cw.toByteArray();
        }
        return classfileBuffer;
    }


    static class MonitorClassVisitor extends ClassVisitor {

        private String classMethodName;

        public MonitorClassVisitor(ClassWriter cw, String classMethodName) {
            super(Opcodes.ASM9, cw);
            this.classMethodName = classMethodName;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
            if (name.equals(classMethodName)) {
                return new MonitorMethodVisitor(mv);
            }
            return mv;
        }

        static class MonitorMethodVisitor extends MethodVisitor{

            public MonitorMethodVisitor(MethodVisitor methodVisitor) {
                super(Opcodes.ASM9, methodVisitor);
            }

            @Override
            public void visitCode() {
                super.visitCode();
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
            }

            @Override
            public void visitInsn(int opcode) {
                if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
                    mv.visitInsn(Opcodes.LSUB);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;", false);
                    mv.visitLdcInsn("Execution time in nanoseconds: ");
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);
                    mv.visitInsn(Opcodes.LLOAD);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long", "toString", "(J)Ljava/lang/String;", false);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                }
                super.visitInsn(opcode);
            }
        }

    }
}

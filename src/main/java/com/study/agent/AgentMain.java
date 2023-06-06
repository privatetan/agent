package com.study.agent;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.util.List;

public class AgentMain {
    public static void main(String[] args) throws Exception {
        List<VirtualMachineDescriptor> listBefore = VirtualMachine.list();

        // agentmain()方法所在jar包
        String jar = "/Users/privatetan/Documents/JavaProjects/agent/target/agent-1.0-SNAPSHOT-jar-with-dependencies.jar";

        for (VirtualMachineDescriptor virtualMachineDescriptor : listBefore) {
            // 针对指定名称的JVM实例
            if (virtualMachineDescriptor.displayName().equals("com.study.agent.PreMain")) {
                System.out.println("将对该进程的vm进行增强：" + virtualMachineDescriptor.displayName() + "的vm进程, " +
                        "pid=" + virtualMachineDescriptor.id());
                // attach到新JVM
                VirtualMachine vm = VirtualMachine.attach(virtualMachineDescriptor);
                // 加载agentmain所在的jar包
                vm.loadAgent(jar);
                // detach
                vm.detach();
            }
        }
    }
}

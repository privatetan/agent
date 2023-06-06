# Java Agent技术案例工程

## Java Agent
Java Agent是运行在Java虚拟机（JVM）上的一个独立程序，它可以在JVM启动时加载并运行。
Java Agent通常可以用来监控、分析或者修改正在运行的Java程序，这些能力可以帮助我们进行性能分析、调试以及安全审计等工作。
Java Agent的使用方式有两种：premain模式和agentmain模式。
- Premain模式是在应用程序启动之前就静态加载Agent，Agent会在应用程序启动时自动执行；
- Agentmain模式是在应用程序启动后通过Attach API动态加载Agent，Agent会在后台线程中执行。

案例实现步骤
1. 
  

## Java Intrumentation API

## JVMTI

## 字节码增强

## 
package ru.nsu.fit.transformers;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class TimeTracingTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        if (!className.contains("TransactionProcessor")) {
            return classfileBuffer;
        }
        byte[] byteCode = classfileBuffer;
        try {
            ClassPool classPool = new ClassPool(true);
            CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
            CtMethod method = ctClass.getDeclaredMethod("processTransaction");
            method.addLocalVariable("startTime", CtClass.longType);
            method.insertBefore("startTime = System.currentTimeMillis();");
            method.insertAfter("System.out.println(\" Time: \"+ (System.currentTimeMillis() - startTime));");
            byteCode = ctClass.toBytecode();
            ctClass.detach();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return byteCode;
    }
}

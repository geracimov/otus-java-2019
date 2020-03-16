package ru.geracimov.otus.java.asm.scanner;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public class ClassScanner extends ClassVisitor {
    private final int api;

    public ClassScanner(int api, ClassWriter cw) {
        super(api, cw);
        this.api = api;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        System.out.println("visitMethod: name=" + name + " descriptor=" + descriptor + " signature=" + signature);
        return new MethodScanner(api);
    }

}

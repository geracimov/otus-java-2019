package ru.geracimov.otus.java.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import ru.geracimov.otus.java.asm.scanner.ClassScanner;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class AsmAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("---=== Mixed agent logic ===---");

        inst.addTransformer(new ClassFileTransformer() {

            @Override
            public byte[] transform(ClassLoader loader,
                                    String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {
                if (className.equals("ru/geracimov/otus/java/GreatBusinessLogic")) {
                    return replaceMethod(classfileBuffer);
                }
                return classfileBuffer;
            }
        });
    }

    private static byte[] replaceMethod(byte[] originalClass) {
        ClassReader cr = new ClassReader(originalClass);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        final ClassScanner visitor = new ClassScanner(Opcodes.ASM7, cw);

        cr.accept(visitor, Opcodes.ASM7);

        byte[] finalClass = cw.toByteArray();

        try (OutputStream fos = new FileOutputStream("GreatBusinessLogic.class")) {
            fos.write(finalClass);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return originalClass;

    }

}



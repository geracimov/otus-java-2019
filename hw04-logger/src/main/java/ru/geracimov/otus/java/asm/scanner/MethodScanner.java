package ru.geracimov.otus.java.asm.scanner;

import org.objectweb.asm.*;
import ru.geracimov.otus.java.Log;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;


public class MethodScanner extends MethodVisitor {
    private String descriptor;

    MethodScanner(int api) {
        super(api);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        System.out.println("visitAnnotation: descriptor=" + descriptor + " " + visible);
        this.descriptor = descriptor;
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public void visitCode() {
        if (Type.getDescriptor(Log.class).equals(descriptor)) {
//            System.out.println(Type.getArgumentsAndReturnSizes(descriptor));
            System.out.println("!!!!!!!!!!");

            Handle handle = new Handle(
                    H_INVOKESTATIC,
                    Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
                    "makeConcatWithConstants",
                    MethodType.methodType(CallSite.class, MethodHandles.Lookup.class,
                            String.class,
                            MethodType.class, String.class, Object[].class)
                            .toMethodDescriptorString(),
                    false);

            visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out",
                    "Ljava/io/PrintStream;");
            visitVarInsn(Opcodes.ILOAD, 1);
            visitInvokeDynamicInsn("makeConcatWithConstants",
                    "(I)Ljava/lang/String;", handle,
                    "executed method: calculation, param: \u0001");

            visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
                    "(Ljava/lang/String;)V", false);
            visitEnd();

        }
        super.visitCode();
    }
}
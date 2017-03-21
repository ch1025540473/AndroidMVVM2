package com.mx.gunit

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * Created by chenbaocheng on 17/3/16.
 */
class ClassTransformer extends ClassVisitor {
    private static final def CONSTRUCTOR_NAMES = new HashSet<String>()
    static {
        CONSTRUCTOR_NAMES.add('<init>')
        CONSTRUCTOR_NAMES.add('<clinit>')
    }

    def className
    def isInterface

    ClassTransformer(int api, ClassVisitor cv) {
        super(api, cv)
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        className = name
        isInterface = (access & (Opcodes.ACC_INTERFACE | Opcodes.ACC_ABSTRACT)) != 0
        super.visit(version, access, name, signature, superName, interfaces)
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (!isInterface && !CONSTRUCTOR_NAMES.contains(name) && !name.contains('$')) {
            //println(className.replaceAll('/', '.') + " . $name # $signature")

            MethodVisitor superVisitor = super.visitMethod(access, name, desc, signature, exceptions)
            return new MethodTransformer(api, className, access, name, desc, signature, exceptions, superVisitor)
        }

        return super.visitMethod(access, name, desc, signature, exceptions)
    }
}

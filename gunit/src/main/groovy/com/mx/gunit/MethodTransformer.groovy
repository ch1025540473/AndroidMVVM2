package com.mx.gunit

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

/**
 * Created by chenbaocheng on 17/3/16.
 */
class MethodTransformer extends MethodVisitor {
    def owner
    def access
    def methodName
    def desc
    def signature
    def exceptions

    MethodTransformer(int api, String owner, int access, String name, String desc, String signature, String[] exceptions, MethodVisitor mv) {
        super(api, mv)
        this.owner = owner
        this.access = access
        this.methodName = name
        this.desc = desc
        this.signature = signature
        this.exceptions = exceptions
    }

    @Override
    void visitCode() {
        super.visitCode()

        mv.visitFieldInsn(
                Opcodes.GETSTATIC,
                Type.getInternalName(System.class),
                'out',
                Type.getDescriptor(PrintStream.class)
        )
        mv.visitLdcInsn("Method = $owner.$methodName $desc $signature".toString())
        mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                Type.getInternalName(PrintStream.class),
                'println',
                Type.getMethodDescriptor(Type.getType(void.class), Type.getType(String.class)),
                false
        )
    }

    @Override
    void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals)
    }
}

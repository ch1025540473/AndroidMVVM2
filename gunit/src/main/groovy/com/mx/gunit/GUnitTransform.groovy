package com.mx.gunit

import com.android.build.api.transform.*
import com.google.common.collect.Sets
import groovy.io.FileType
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

/**
 * Created by chenbaocheng on 17/3/16.
 */
class GUnitTransform extends Transform {

    @Override
    String getName() {
        return 'gunit'
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return Sets.immutableEnumSet(QualifiedContent.DefaultContentType.CLASSES)
    }

    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return Sets.immutableEnumSet(
                QualifiedContent.Scope.PROJECT
                , QualifiedContent.Scope.PROJECT_LOCAL_DEPS
                //,QualifiedContent.Scope.EXTERNAL_LIBRARIES
        )
    }

    @Override
    boolean isIncremental() {
        return true
    }

    static void processClassFile(File file, File outFile) throws IOException {
        println "In class file = " + file.absolutePath

        byte[] fileBytes;
        if (file.name.toLowerCase(Locale.US).endsWith('.class')) {
            ClassReader reader = new ClassReader(file.bytes)
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES) {
                @Override
                protected String getCommonSuperClass(String type1, String type2) {
                    if (type1 == type2) {
                        return type1
                    }

                    try {
                        return super.getCommonSuperClass(type1, type2)
                    } catch (Exception e) {
                        return "java/lang/Object"
                    }
                }
            }
            ClassTransformer transformer = new ClassTransformer(Opcodes.ASM5, writer)
            reader.accept(transformer, 0)
            fileBytes = writer.toByteArray()
        } else {
            fileBytes = file.bytes
        }

        println "Out class file = " + outFile.absolutePath
        outFile.delete()
        outFile.parentFile?.mkdirs()
        outFile.bytes = fileBytes;
    }

    static void processDir(File inDir, File outDir) throws IOException {
        inDir.eachFileRecurse(FileType.FILES, { file ->
            String outFilePath = file.absolutePath.replaceFirst(inDir.absolutePath, outDir.absolutePath)
            processClassFile(file, new File(outFilePath))
        })
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        transformInvocation.getInputs().forEach({ input ->
            input.directoryInputs.forEach({ dir ->
                File outDir = transformInvocation.outputProvider.getContentLocation(dir.name, outputTypes, scopes, Format.DIRECTORY)
                println "outDir = " + outDir.absolutePath
                processDir(dir.file, outDir)
            })
        })
    }
}

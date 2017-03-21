package com.mx.gunit

import org.gradle.api.Plugin
import org.gradle.api.Project

class GUnitPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {

        def android = project.getExtensions().findByName('android')
        println "Project(${project}) --- ext = ${android}"
        if (android != null) {
            android.registerTransform(new GUnitTransform())
        }
    }
}
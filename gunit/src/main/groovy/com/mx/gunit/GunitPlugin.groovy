import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class GunitTransform extends Transform {

    @Override
    String getName() {
        return 'META-INFO'
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        Set<QualifiedContent.ContentType> types = new HashSet<>();
        types.add(QualifiedContent.DefaultContentType.CLASSES)
        return types;
    }

    @Override
    Set<QualifiedContent.Scope> getScopes() {
        Set<QualifiedContent.Scope> scopes = new HashSet<>();
        scopes.add(QualifiedContent.Scope.TESTED_CODE)
        return scopes;
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        transformInvocation.getInputs().forEach({ input ->
            input.directoryInputs.forEach({ dir ->
                println('dirinput: ' + dir)
            })
        })
    }
}

public class GunitPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def plugins = project.getPlugins();
        if(plugins.findPlugin(AppPlugin)) {
            def android = project.extensions.getByType(AppExtension)
            android.registerTransform(new GunitTransform())
        }
    }
}
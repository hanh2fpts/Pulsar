import com.hmst.pulsar.buildlogic.libs
import com.hmst.pulsar.buildlogic.library
import com.hmst.pulsar.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/** Hilt + KSP for any module that declares @Module, @Inject or @HiltViewModel */
class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("ksp"))
        pluginManager.apply(libs.pluginId("hilt"))

        dependencies {
            add("implementation", libs.library("hilt-android"))
            add("ksp", libs.library("hilt-compiler"))
        }
    }
}

import com.hmst.pulsar.buildlogic.libs
import com.hmst.pulsar.buildlogic.library
import com.hmst.pulsar.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/** Room3 runtime + compiler (KSP) */
class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("ksp"))

        dependencies {
            add("implementation", libs.library("androidx-room3-runtime"))
            add("ksp", libs.library("androidx-room3-compiler"))
        }
    }
}

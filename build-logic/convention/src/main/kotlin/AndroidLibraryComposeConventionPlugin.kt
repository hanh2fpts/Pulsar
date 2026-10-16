import com.android.build.api.dsl.LibraryExtension
import com.hmst.pulsar.buildlogic.addComposeDependencies
import com.hmst.pulsar.buildlogic.libs
import com.hmst.pulsar.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** Android library with Compose: core:designsystem and every feature */
class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("pulsar-android-library"))
        pluginManager.apply(libs.pluginId("kotlin-compose"))

        extensions.configure<LibraryExtension> {
            buildFeatures { compose = true }
        }
        addComposeDependencies()
    }
}

import com.hmst.pulsar.buildlogic.libs
import com.hmst.pulsar.buildlogic.library
import com.hmst.pulsar.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * A feature module: Compose UI + Hilt ViewModel + type-safe navigation.
 * Features depend on core modules only, never on another feature.
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("pulsar-android-library-compose"))
        pluginManager.apply(libs.pluginId("pulsar-hilt"))
        pluginManager.apply(libs.pluginId("kotlin-serialization"))

        dependencies {
            add("implementation", project(":core:designsystem"))
            add("implementation", libs.library("androidx-navigation-compose"))
            add("implementation", libs.library("androidx-hilt-navigation-compose"))
            add("implementation", libs.library("androidx-lifecycle-runtime-compose"))
            add("implementation", libs.library("androidx-lifecycle-viewmodel-compose"))
        }
    }
}

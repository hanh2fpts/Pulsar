import com.android.build.api.dsl.ApplicationExtension
import com.hmst.pulsar.buildlogic.addComposeDependencies
import com.hmst.pulsar.buildlogic.configureAndroid
import com.hmst.pulsar.buildlogic.libs
import com.hmst.pulsar.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** :app — Android application with Compose */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("android-application"))
        pluginManager.apply(libs.pluginId("kotlin-compose"))

        extensions.configure<ApplicationExtension> {
            configureAndroid()
            buildFeatures { compose = true }
        }
        addComposeDependencies()
    }
}

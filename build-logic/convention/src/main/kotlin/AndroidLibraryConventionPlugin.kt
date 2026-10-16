import com.android.build.api.dsl.LibraryExtension
import com.hmst.pulsar.buildlogic.configureAndroid
import com.hmst.pulsar.buildlogic.libs
import com.hmst.pulsar.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** Plain Android library (no UI): core:database, core:network, core:datastore... */
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("android-library"))

        extensions.configure<LibraryExtension> {
            configureAndroid()
        }
    }
}

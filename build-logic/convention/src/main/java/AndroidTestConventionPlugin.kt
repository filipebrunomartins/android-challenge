import com.android.build.api.dsl.TestExtension
import com.challenge.movieflux.buildlogic.configureGradleManagedDevices
import com.challenge.movieflux.buildlogic.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.test")

            extensions.configure<TestExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 36
                configureGradleManagedDevices(this)
            }
        }
    }
}
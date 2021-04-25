import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

fun <T> NamedDomainObjectContainer<T>.debug(action: T.() -> Unit) {
    maybeCreate("debug").action()
}

fun <T> NamedDomainObjectContainer<T>.qa(action: T.() -> Unit) {
    maybeCreate("qa").action()
}

fun <T> NamedDomainObjectContainer<T>.release(action: T.() -> Unit) {
    maybeCreate("release").action()
}

fun PluginDependenciesSpec.android(module: String): PluginDependencySpec =
    id("com.android.$module")

fun PluginDependenciesSpec.androidx(module: String): PluginDependencySpec =
    id("androidx.$module")

fun PluginDependenciesSpec.google(module: String): PluginDependencySpec =
    id("com.google.gms.$module")

fun PluginDependenciesSpec.firebase(module: String): PluginDependencySpec =
    id("com.google.firebase.$module")

fun DependencyHandler.`qaImplementation`(dependencyNotation: Any): Dependency? =
    add("qaImplementation", dependencyNotation)
package frontier.skf

import frontier.skf.simple.SimpleCoroutineManager
import frontier.skf.simple.SimpleFeatureCoroutineManager
import kotlinx.coroutines.CoroutineDispatcher
import ninja.leaping.configurate.ConfigurationNode
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.startCoroutine

class FeatureManager(private val root: ConfigurationNode,
                     private val dispatcher: CoroutineDispatcher) {

    companion object {
        private const val ENABLED_NODE = "-enabled-features"
    }

    private val features = LinkedHashMap<String, Feature>()
    private val coroutineManager = SimpleCoroutineManager()

    fun addFeature(id: String, enabledByDefault: Boolean = true, executor: FeatureExecutor) {
        features[id] = Feature(id, enabledByDefault, executor)

        val featureNode = root.getNode(id)

        executor.startCoroutine(
            SimpleFeatureCoroutineManager(coroutineManager, featureNode),
            Continuation(dispatcher) {})
    }

    fun setupDefaults() {
        val enabledNode = root.getNode(ENABLED_NODE)

        if (enabledNode.isVirtual) {
            val defaultEnabled = features.values.mapNotNullTo(arrayListOf()) { feature ->
                feature.takeIf { it.enabledByDefault }?.id
            }
            defaultEnabled.sort()

            enabledNode.value = defaultEnabled
        }

        for (continuation in coroutineManager.continuations[FeatureState.SETUP_DEFAULTS]) {
            continuation.resume(Unit)
        }
    }

    fun initialize() {
        for (continuation in coroutineManager.continuations[FeatureState.INITIALIZATION]) {
            continuation.resume(Unit)
        }
    }

    fun shutdown() {
        for (continuation in coroutineManager.continuations[FeatureState.SHUTDOWN]) {
            continuation.resume(Unit)
        }
    }
}
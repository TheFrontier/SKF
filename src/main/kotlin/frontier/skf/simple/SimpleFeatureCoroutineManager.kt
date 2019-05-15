package frontier.skf.simple

import frontier.skf.FeatureCoroutineManager
import frontier.skf.FeatureState
import ninja.leaping.configurate.ConfigurationNode
import kotlin.coroutines.suspendCoroutine

class SimpleFeatureCoroutineManager(private val manager: SimpleCoroutineManager,
                                    override val id: String) : FeatureCoroutineManager {

    override suspend fun waitFor(state: FeatureState): ConfigurationNode = suspendCoroutine {
        manager.continuations.put(state, id to it)
    }
}
package frontier.skf.simple

import frontier.skf.FeatureCoroutineManager
import frontier.skf.FeatureState
import ninja.leaping.configurate.ConfigurationNode
import kotlin.coroutines.suspendCoroutine

class SimpleFeatureCoroutineManager(private val manager: SimpleCoroutineManager,
                                    override val node: ConfigurationNode) : FeatureCoroutineManager {

    override suspend fun waitFor(state: FeatureState) = suspendCoroutine<Unit> {
        manager.continuations.put(state, it)
    }
}
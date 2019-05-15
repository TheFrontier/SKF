package frontier.skf

import ninja.leaping.configurate.ConfigurationNode
import kotlin.coroutines.RestrictsSuspension

@RestrictsSuspension
interface FeatureCoroutineManager {

    val node: ConfigurationNode

    suspend fun waitFor(state: FeatureState)
}
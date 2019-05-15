package frontier.skf

import ninja.leaping.configurate.ConfigurationNode
import kotlin.coroutines.RestrictsSuspension

@RestrictsSuspension
interface FeatureCoroutineManager {

    val id: String

    suspend fun waitFor(state: FeatureState): ConfigurationNode
}
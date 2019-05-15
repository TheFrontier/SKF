package frontier.skf.simple

import com.google.common.collect.ArrayListMultimap
import frontier.skf.FeatureState
import ninja.leaping.configurate.ConfigurationNode
import kotlin.coroutines.Continuation

class SimpleCoroutineManager {

    internal val continuations = ArrayListMultimap.create<FeatureState, Pair<String, Continuation<ConfigurationNode>>>()
}
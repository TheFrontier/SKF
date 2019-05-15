package frontier.skf.simple

import com.google.common.collect.ArrayListMultimap
import frontier.skf.FeatureState
import kotlin.coroutines.Continuation

class SimpleCoroutineManager {

    internal val continuations = ArrayListMultimap.create<FeatureState, Continuation<Unit>>()
}
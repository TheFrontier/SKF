package frontier.skf

data class Feature(val id: String, val enabledByDefault: Boolean, val executor: FeatureExecutor)

typealias FeatureExecutor = suspend FeatureCoroutineManager.() -> Unit
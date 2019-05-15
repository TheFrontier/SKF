# Sponge Kotlin Features

A simple Feature API for managing config-regulated plugin features.

### A Simple Example (also featuring SKPC)

```kotlin
import frontier.skf.FeatureManager
import frontier.skf.FeatureState
import frontier.skpc.CommandTree
import frontier.skpc.util.component1
import frontier.skpc.util.mustBeSubtype
import frontier.skpc.value.standard.ValueParameters.player
import kotlinx.coroutines.asCoroutineDispatcher
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.loader.ConfigurationLoader
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.config.DefaultConfig
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameInitializationEvent
import org.spongepowered.api.event.game.state.GamePreInitializationEvent
import org.spongepowered.api.event.game.state.GameStoppingEvent
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.text.Text

@Plugin(id = "my-plugin")
class MyPlugin constructor(@param:DefaultConfig(sharedRoot = false) val loader: ConfigurationLoader<CommentedConfigurationNode>) {

    private val manager: FeatureManager =
        FeatureManager(Sponge.getScheduler().createSyncExecutor(this).asCoroutineDispatcher()) { loader.load() }

    @Listener
    fun onPreInit(event: GamePreInitializationEvent) {
        manager.addFeature("heal", enabledByDefault = true) {
            val setupNode = waitFor(FeatureState.SETUP_DEFAULTS)

            val defaultHealthNode = setupNode.getNode("default-max-health")

            if (defaultHealthNode.isVirtual) {
                defaultHealthNode.value = 20.0
            }

            val initNode = waitFor(FeatureState.INITIALIZATION)

            val defaultMaxHealth = initNode.getNode("default-max-health").double

            val command = CommandTree.Root("heal").apply {
                "other" / player("player") execute { src, (player) ->
                    player.offer(Keys.HEALTH, player.get(Keys.MAX_HEALTH).orElse(defaultMaxHealth))
                    src.sendMessage(Text.of("${player.name} has been healed!"))

                    CommandResult.success()
                }

                this execute mustBeSubtype { player: Player, _ ->
                    player.offer(Keys.HEALTH, player.get(Keys.MAX_HEALTH).orElse(defaultMaxHealth))
                    player.sendMessage(Text.of("You'ver has been healed!"))

                    CommandResult.success()
                }
            }

            Sponge.getCommandManager().register(this@MyPlugin, command.toCallable(), command.aliases)
        }

        manager.setupDefaults()
    }

    @Listener
    fun onInit(event: GameInitializationEvent) {
        manager.initialize()
    }

    @Listener
    fun onStopping(event: GameStoppingEvent) {
        manager.shutdown()
    }
}
```
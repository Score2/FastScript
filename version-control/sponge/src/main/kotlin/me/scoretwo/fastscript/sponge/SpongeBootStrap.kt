package me.scoretwo.fastscript.sponge

import com.google.inject.Inject
import me.scoretwo.utils.sponge.plugin.toGlobalPlugin
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameInitializationEvent
import org.spongepowered.api.event.game.state.GamePreInitializationEvent
import org.spongepowered.api.event.game.state.GameStoppingEvent
import org.spongepowered.api.plugin.Dependency
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer

@Plugin(
    id = "%%id%%",
    name = "%%name%%",
    authors = ["Score2"],
    description = "%%description%%",
    dependencies = [Dependency(id = "placeholderapi", optional = true)],
    version = "%%version%%"
)
class SpongeBootStrap {

    @Inject
    lateinit var pluginContainer: PluginContainer

    val spongePlugin = SpongePlugin(pluginContainer.toGlobalPlugin())

    @Listener
    fun onLoad(e: GamePreInitializationEvent) {
        spongePlugin.load()
    }

    @Listener
    fun onEnable(e: GameInitializationEvent) {
        spongePlugin.enable()
    }

    @Listener
    fun onDisable(e: GameStoppingEvent) {
        spongePlugin.disable()
    }

}
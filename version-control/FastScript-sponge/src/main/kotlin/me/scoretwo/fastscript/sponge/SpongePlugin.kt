package me.scoretwo.fastscript.sponge

import me.scoretwo.fastscript.FastScript
import me.scoretwo.fastscript.api.format.FormatHeader
import me.scoretwo.fastscript.api.plugin.ScriptPlugin
import me.scoretwo.fastscript.api.utils.ExecType
import me.scoretwo.fastscript.api.utils.maven.MavenArtifact
import me.scoretwo.fastscript.sendMessage
import me.scoretwo.fastscript.sponge.hook.PlaceholderAPIHook
import me.scoretwo.utils.plugin.GlobalPlugin
import me.scoretwo.utils.sender.GlobalPlayer
import me.scoretwo.utils.sender.GlobalSender
import me.scoretwo.utils.sponge.command.registerSpongeCommands
import me.scoretwo.utils.sponge.command.toGlobalPlayer
import me.scoretwo.utils.sponge.command.toSpongePlayer
import me.scoretwo.utils.sponge.command.toSpongeSender
import me.scoretwo.utils.sponge.plugin.toSpongePlugin
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player

class SpongePlugin(plugin: GlobalPlugin): ScriptPlugin(plugin) {

    override val libs = mutableListOf(MavenArtifact("net.md-5:bungeecord-api:1.16-R0.4", "https://maven.aliyun.com/repository/central"))

    override fun load() {
        FastScript.setBootstrap(this)
    }

    override fun enable() {
        FastScript.instance.reload("script", "plugin")
        PlaceholderAPIHook.initializePlaceholder()
    }

    override fun setPlaceholder(player: GlobalPlayer, string: String): String {
        return PlaceholderAPIHook.parsePlaceholder(string, player.toSpongePlayer())
    }

    override fun toOriginalPlugin() = toSpongePlugin()

    override fun toOriginalSender(sender: GlobalSender) = sender.toSpongeSender()

    override fun toOriginalPlayer(player: GlobalPlayer) = player.toSpongePlayer()

    override fun toOriginalServer(): Any? = Sponge.getServer()
    override fun toGlobalPlayer(any: Any?): GlobalPlayer {
        if (any !is Player) {
            throw Exception("$any not a player!")
        }
        return any.toGlobalPlayer()
    }

    override fun toGlobalSender(any: Any?): GlobalSender {
        if (any !is Player) {
            throw Exception("$any not a player!")
        }
        return any.toGlobalPlayer()
    }

    override fun registerListener(any: Any?): Boolean = try {
        Sponge.getEventManager().registerListeners(toSpongePlugin(), any ?: false)
        true
    } catch (t: Throwable) {
        false
    }

    override fun unregisterListener(any: Any?): Boolean = try {
        Sponge.getEventManager().unregisterListeners(any ?: false)
        true
    } catch (t: Throwable) {
        false
    }

    override fun reload() {
        if (PlaceholderAPIHook.initializePlaceholder()) {
            plugin.server.console.sendMessage(FormatHeader.HOOKED, "成功挂钩 §ePlaceholderAPI!")
        }
    }
}
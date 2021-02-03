package me.scoretwo.fastscript.velocity

import com.google.inject.Inject
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import me.scoretwo.fastscript.FastScript
import me.scoretwo.fastscript.api.plugin.ScriptPlugin
import me.scoretwo.utils.plugin.GlobalPlugin
import me.scoretwo.utils.sender.GlobalPlayer
import me.scoretwo.utils.sender.GlobalSender
import me.scoretwo.utils.velocity.command.registerVelocityCommands
import me.scoretwo.utils.velocity.command.toVelocityPlayer
import me.scoretwo.utils.velocity.command.toVelocitySender
import me.scoretwo.utils.velocity.server.proxyServer
import net.md_5.bungee.api.ChatColor
import java.io.File
import java.nio.file.Path
import java.util.logging.Logger

class VelocityPlugin(val plugin: GlobalPlugin): ScriptPlugin(plugin) {

    override fun load() {
        FastScript.setBootstrap(this)
    }

    override fun enable() {
        FastScript.instance.onReload()

        FastScript.instance.commandNexus.registerVelocityCommands()
    }

    override fun setPlaceholder(player: GlobalPlayer, string: String): String {
        return string
    }

    override fun toOriginalSender(sender: GlobalSender) = sender.toVelocitySender()

    override fun toOriginalPlayer(player: GlobalPlayer) = player.toVelocityPlayer()

    override fun toOriginalServer() = proxyServer

    override fun reload() {

    }
}
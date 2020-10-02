package me.scoretwo.fastscript.bukkit

import me.scoretwo.fastscript.FastScript
import me.scoretwo.fastscript.api.plugin.FastScriptMain
import me.scoretwo.fastscript.bukkit.hooks.PlaceholderAPIHook
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.command.SimpleCommandMap
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class BukkitSection: JavaPlugin(), FastScriptMain {

    override fun onLoad() {
        FastScript.setBootstrap(this)
    }

    override fun onEnable() {
        FastScript.instance.onReload()

        // 暂无计划
        val metrics = Metrics(this, 9014)

        commandMap.register(description.name, object : Command(description.name, "", "/" + description.name, listOf("script","bukkitScript")) {
            override fun execute(sender: CommandSender, label: String, args: Array<String>): Boolean {
                FastScript.instance.commandManager.execute(sender, args)
                return true
            }

            override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>): MutableList<String> {
                return FastScript.instance.commandManager.tabComplete(sender, args)
            }
        })
    }

    override val CONSOLE: Any = Bukkit.getConsoleSender()

    override fun getPluginClassLoader(): ClassLoader {
        return super.getClassLoader()
    }

    override fun setPlaceholder(player: Any, string: String): String {
        var text: String = string
        if (PAPIHook != null) {
            text = PlaceholderAPIHook.setPlaceholder(player as? Player, string)
        }

        return text
    }

    override fun sendMessage(sender: Any, string: String, colorIndex: Boolean) {
        asSender(sender)?.sendMessage(if (colorIndex) ChatColor.translateAlternateColorCodes('&', string) else string)
    }

    override fun hasPermission(sender: Any, string: String): Boolean {
        return asSender(sender)?.hasPermission(string)!!
    }

    fun asSender(sender: Any): CommandSender? {
        return sender as? CommandSender
    }

    override fun onReload() {
        if (PAPIHook == null) {
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                PAPIHook = PlaceholderAPIHook(this)
                FastScript.sendMessage(CONSOLE,"&7[&2Fast&aScript&7] &6HOOKED &8| &2成功挂钩 PlaceholderAPI!")
            }
        }
    }

    companion object {
        private var PAPIHook: PlaceholderAPIHook? = null

        val commandMap: CommandMap = Bukkit.getServer().javaClass.getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer()) as SimpleCommandMap
    }

}
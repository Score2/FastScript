package me.scoretwo.fastscript.bukkit.hook

import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.scoretwo.fastscript.api.placeholder.Placeholders
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class PlaceholderAPIHook(val javaPlugin: JavaPlugin): PlaceholderExpansion() {

    override fun persist(): Boolean {
        return true
    }

    override fun getIdentifier(): String {
        return javaPlugin.description.name.toLowerCase()
    }

    override fun getAuthor(): String {
        return javaPlugin.description.authors.toString()
    }

    override fun getVersion(): String {
        return javaPlugin.description.version
    }

    override fun onPlaceholderRequest(player: Player, params: String): String {
        return Placeholders.parse(player, params)
    }

    companion object {
        fun setPlaceholder(player: Player?, string: String): String = PlaceholderAPI.setPlaceholders(player, string)
    }
}
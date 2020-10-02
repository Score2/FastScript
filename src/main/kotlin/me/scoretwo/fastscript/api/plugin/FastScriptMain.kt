package me.scoretwo.fastscript.api.plugin

import com.sun.xml.internal.ws.util.StreamUtils
import me.scoretwo.fastscript.utils.FileUtils
import java.io.File
import java.io.InputStream

interface FastScriptMain {

    val CONSOLE: Any

    fun getDataFolder(): File

    fun getPluginClassLoader(): ClassLoader

    fun setPlaceholder(player: Any, string: String): String

    fun onReload()

    fun sendMessage(sender: Any, string: String, colorIndex: Boolean)


}
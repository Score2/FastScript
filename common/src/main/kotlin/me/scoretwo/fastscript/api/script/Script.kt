package me.scoretwo.fastscript.api.script

import me.scoretwo.fastscript.*
import me.scoretwo.fastscript.api.expansion.FastScriptExpansion
import me.scoretwo.fastscript.api.format.FormatHeader
import me.scoretwo.utils.sender.GlobalSender

/**
 * @author Scre2
 * @date 2021/2/8 13:33
 *
 * @project FastScript
 */
abstract class Script(
    val name: String,
    val option: ScriptOption,
    // sign, text
    var texts : MutableMap<String, String> = mutableMapOf()
) {

    val main get() = option.getString("main") ?: "main"
    val version get() = option.getString("version") ?: "1.0-default"
    val description get() = option.getString("description") ?: "Not more..."
    val authors get() = option.getStringList("authors") ?: listOf()

    class Init(val option: ScriptOption) {
        val useAsync get() = option.getBoolean("init.use-async")
        val protected get() = option.getBoolean("init.protected")
    }
    val init = Init(option)

    val listeners = mutableListOf<Any?>()

    fun bindExpansions() =
        mutableListOf<FastScriptExpansion>().also { expansions ->
            texts.keys.forEach {
                expansions.add(FastScript.instance.expansionManager.getExpansionBySign(it) ?: return@forEach)
            }
        }

    open fun eval(sign: String, sender: GlobalSender, vararg args: String): Any? =
        eval(FastScript.instance.expansionManager.getExpansionBySign(sign), sender, *args)

    fun eval(expansion: FastScriptExpansion?, sender: GlobalSender, vararg args: String): Any? =
        expansion?.eval(this, sender, arrayOf(*args))

    open fun execute(sign: String, sender: GlobalSender, main: String = option.main, args: Array<Any?> = arrayOf()): Any? =
        execute(FastScript.instance.expansionManager.getExpansionBySign(sign), sender, main, args)

    fun execute(expansion: FastScriptExpansion?, sender: GlobalSender, main: String = option.main, args: Array<Any?> = arrayOf()): Any? =
        expansion?.execute(this, sender, main, args)

    fun eval(script: String, sign: String, sender: GlobalSender, vararg args: String) = let {
        FastScript.instance.scriptManager.eval(FastScript.instance.scriptManager.getScript(script) ?: return@let null, sign, sender, *args)
    }

    fun execute(script: String, sign: String, sender: GlobalSender, main: String = FastScript.instance.scriptManager.getScript(script)?.configOption?.main ?: "main", args: Array<Any?> = arrayOf()) = let {
        FastScript.instance.scriptManager.execute(FastScript.instance.scriptManager.getScript(script) ?: return@let null, sign, sender, main, args)
    }

    fun registerListener(any: Any?) {
        if (!plugin.registerListener(any))
            plugin.server.console.sendMessage(
                FormatHeader.WARN,
                languages["SCRIPT.LISTENERS.FAILED-REGISTER"].setPlaceholder("script_name" to name)
            )
        else
            listeners.add(any)
    }

    fun unregisterListener(any: Any?) {
        if (!plugin.unregisterListener(any))
            plugin.server.console.sendMessage(
                FormatHeader.WARN,
                languages["SCRIPT.LISTENERS.FAILED-UNREGISTER"].setPlaceholder("script_name" to name)
            )
        else
            listeners.remove(any)
    }

    fun unregisterListeners() {
        listeners.toMutableList().forEach { unregisterListener(it) }
    }
}
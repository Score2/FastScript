package me.scoretwo.fastscript.command.commands

import me.scoretwo.fastscript.sendMessage
import me.scoretwo.fastscript.FastScript
import me.scoretwo.fastscript.api.format.FormatHeader
import me.scoretwo.fastscript.command.SimpleCommand
import me.scoretwo.fastscript.languages
import me.scoretwo.utils.sender.GlobalSender
import me.scoretwo.utils.server.task.TaskType

/**
 * @author Score2
 * @date 2021/2/6 14:50
 *
 * @project FastScript
 */
class ReloadCommand: SimpleCommand(arrayOf("reload")) {

    override var description = languages["COMMAND-NEXUS.COMMANDS.RELOAD.DESCRIPTION"]

    override var customCommands: MutableMap<String, Pair<Array<String>?, String>> = mutableMapOf(
        "all" to Pair(null, languages["COMMAND-NEXUS.COMMANDS.RELOAD.MODE.ALL"]),
        "config" to Pair(null, languages["COMMAND-NEXUS.COMMANDS.RELOAD.MODE.CONFIG"]),
        "script" to Pair(null, languages["COMMAND-NEXUS.COMMANDS.RELOAD.MODE.SCRIPT"]),
        "plugin" to Pair(null, languages["COMMAND-NEXUS.COMMANDS.RELOAD.MODE.PLUGIN"]),
    )

    override fun execute(sender: GlobalSender, parents: Array<String>, args: Array<String>): Boolean {
        var mode = "all"
        if (args.isNotEmpty()) {
            mode = args[0].toLowerCase()
        }

        if (args.size > 1 && args[1] == ":async") {
            plugin.server.schedule.task(plugin, TaskType.ASYNC, Runnable {
                if (mode == "all") {
                    FastScript.instance.reloadAll()
                    sender.sendMessage(FormatHeader.INFO, languages["COMMAND-NEXUS.COMMANDS.RELOAD.ASYNC-LOADED-ALL"])
                    return@Runnable
                }
                FastScript.instance.reload(mode)
                when (mode) {
                    "config" -> sender.sendMessage(FormatHeader.INFO, languages["COMMAND-NEXUS.COMMANDS.RELOAD.ASYNC-LOADED-CONFIG"])
                    "script" -> sender.sendMessage(FormatHeader.INFO, languages["COMMAND-NEXUS.COMMANDS.RELOAD.ASYNC-LOADED-SCRIPT"])
                    "plugin" -> sender.sendMessage(FormatHeader.INFO, languages["COMMAND-NEXUS.COMMANDS.RELOAD.ASYNC-LOADED-PLUGIN"])
                }
            })

            return true
        }


        if (mode == "all") {
            FastScript.instance.reloadAll()
            sender.sendMessage(FormatHeader.INFO, languages["COMMAND-NEXUS.COMMANDS.RELOAD.LOADED-ALL"])
            return true
        }
        FastScript.instance.reload(mode)
        when (mode) {
            "config" -> sender.sendMessage(FormatHeader.INFO, languages["COMMAND-NEXUS.COMMANDS.RELOAD.LOADED-CONFIG"])
            "script" -> sender.sendMessage(FormatHeader.INFO, languages["COMMAND-NEXUS.COMMANDS.RELOAD.LOADED-SCRIPT"])
            "plugin" -> sender.sendMessage(FormatHeader.INFO, languages["COMMAND-NEXUS.COMMANDS.RELOAD.LOADED-PLUGIN"])
        }

        return true
    }

    override fun tabComplete(sender: GlobalSender, parents: Array<String>, args: Array<String>) =
        when {
            args.size < 2 -> mutableListOf("config", "script", "plugin", "all")
            args.size < 3 -> mutableListOf(":async")
            else -> null
        }


}
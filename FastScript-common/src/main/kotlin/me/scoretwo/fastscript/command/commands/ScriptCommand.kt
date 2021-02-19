package me.scoretwo.fastscript.command.commands

import me.scoretwo.fastscript.FastScript
import me.scoretwo.fastscript.api.format.FormatHeader
import me.scoretwo.fastscript.command.SimpleCommand
import me.scoretwo.fastscript.sendMessage
import me.scoretwo.utils.command.SubCommand
import me.scoretwo.utils.command.executor.CommandExecutor
import me.scoretwo.utils.command.executor.Executors
import me.scoretwo.utils.sender.GlobalSender

/**
 * @author Score2
 * @date 2021/1/3 21:42
 *
 * @project FastScript
 */
class ScriptCommand: SimpleCommand(arrayOf("script")) {

    override var description = "操作脚本重载/评估/运行."

    private val runCommand = nextBuilder()
        .alias("run")
        .description("执行这个脚本并得到返回值(:s 加在最后不返回消息)")
        .executor(object : Executors {
            override fun execute(sender: GlobalSender, parents: Array<String>, args: Array<String>): Boolean {
                val noReturn = if (args.isEmpty()) false else args[args.size - 1] == ":s"

                val script = FastScript.instance.scriptManager.scripts[parents[parents.size - 2]] ?: let {
                    // 似乎不会发生?
                    sender.sendMessage(FormatHeader.WARN, "没有找到目标脚本 §c${parents[parents.size - 2]}§7! 请检查名称.")
                    return true
                }
                if (args.isEmpty()) {
                    sender.sendMessage(FormatHeader.ERROR, "正确用法: /${parents.joinToString(" ")} §f<${script.texts.keys.joinToString("/")}> §7<${script.option.main}> <args...>")
                    return true
                }
                val sign = args[0]
                if (!script.texts.keys.contains(sign)) {
                    sender.sendMessage(FormatHeader.ERROR, "指定的拓展标识 §6$sign §7不正确! 正确用法: /${parents.joinToString(" ")} §f<${script.texts.keys.joinToString("/")}> §7<${script.option.main}> <args...>")
                    return true
                }

                val args0: Array<Any?> = when {
                    args.size < 2 -> arrayOf()
                    noReturn -> arrayOf(*args.sliceArray(2..args.size - 2))
                    else -> arrayOf(*args.sliceArray(2 until args.size))
                }

                val result = when {
                    args.isEmpty() -> script.execute(sign, sender)
                    args.size >= 2 -> script.execute(sign, sender, args[1], args0)
                    else -> null
                }

                if (!noReturn) {
                    sender.sendMessage(FormatHeader.INFO, "脚本 §b${script.name} §7的运行结果: §b${result}")
                }

                return true
            }

            override fun tabComplete(sender: GlobalSender, parents: Array<String>, args: Array<String>) = mutableListOf(":s")
        })
        .build()

    private val evaluateCommand = nextBuilder()
        .alias("eval")
        .description("评估这个脚本并获得返回值(:s 加在最后不返回消息)")
        .executor(object : Executors {
            override fun execute(sender: GlobalSender, parents: Array<String>, args: Array<String>): Boolean {
                val noReturn = if (args.isEmpty()) false else args[args.size - 1] == ":s"

                val script = FastScript.instance.scriptManager.scripts[parents[parents.size - 2]] ?: let {
                    // 似乎不会发生?
                    sender.sendMessage(FormatHeader.WARN, "没有找到目标脚本 §c${parents[parents.size - 2]}§7! 请检查名称.")
                    return true
                }
                if (args.isEmpty()) {
                    sender.sendMessage(FormatHeader.ERROR, "正确用法: /${parents.joinToString(" ")} §f<${script.texts.keys.joinToString("/")}>")
                    return true
                }
                val sign = args[0]
                if (!script.texts.keys.contains(sign)) {
                    sender.sendMessage(FormatHeader.ERROR, "指定的拓展标识 §6$sign §7不正确! 正确用法: /${parents.joinToString(" ")} §f<${script.texts.keys.joinToString("/")}>")
                    return true
                }

                val result = script.eval(sign, sender)

                if (!noReturn) {
                    sender.sendMessage(FormatHeader.INFO, "脚本 §b${script.name} §7的评估结果: §b${result}")
                }

                return true
            }

            override fun tabComplete(sender: GlobalSender, parents: Array<String>, args: Array<String>) = mutableListOf(":s")
        })
        .build()

    private val reloadCommand = nextBuilder()
        .alias("reload")
        .description("重新载入这个脚本")
        .execute(object : CommandExecutor {
            override fun execute(sender: GlobalSender, parents: Array<String>, args: Array<String>): Boolean {
                val script = FastScript.instance.scriptManager.scripts[parents[parents.size - 2]] ?: let {
                    // 似乎不会发生?
                    sender.sendMessage(FormatHeader.WARN, "没有找到目标脚本 §c${parents[parents.size - 2]}§7! 请检查名称.")
                    return true
                }

                script.reload()
                sender.sendMessage(FormatHeader.INFO, "脚本 §b${script.name} §7的配置文件重新载入完成.")
                return true
            }
        })
        .build()

    private val infoCommand = nextBuilder()
        .alias("info")
        .description("显示有关这个脚本的信息")
        .execute(object : CommandExecutor {
            override fun execute(sender: GlobalSender, parents: Array<String>, args: Array<String>): Boolean {
                val script = FastScript.instance.scriptManager.scripts[parents[parents.size - 2]] ?: let {
                    // 似乎不会发生?
                    sender.sendMessage(FormatHeader.WARN, "没有找到目标脚本 §c${parents[parents.size - 2]}§7! 请检查名称.")
                    return true
                }

                sender.sendMessage(FormatHeader.INFO, StringBuilder("脚本 §b${script.name} §7的相关信息:\n").also { builder ->
                    script.description.version?.also {
                        builder.append("  §3§l* §7版本: §2$it\n")
                    }
                    script.description.authors.let {
                        if (it.isEmpty())
                            return@let
                        builder.append("  §3§l* §7编写者: §3${it.joinToString("§7, §3")}\n")
                    }
                    script.description.description?.also {
                        builder.append("  §3§l* §7描述: §f$it\n")
                    }
                    builder.append("  §3§l* §7主函数: §a${script.description.main}\n")
                    script.bindExpansions().let { expansions ->
                        if (expansions.isEmpty())
                            return@let
                        val signs = mutableListOf<String>().also { signs -> expansions.forEach { expansion -> signs.add(expansion.sign) } }

                        builder.append("  §3§l* §7拓展: §6${signs.joinToString("§7, §6")}\n")
                    }
                }.toString())
                return true
            }
        })
        .build()

    fun reload() {
        subCommands.clear()
        FastScript.instance.scriptManager.scripts.forEach {
            val subCommand = nextBuilder()
                .alias(it.value.description.name)
                .description("一个自定义脚本")
                .execute(object : CommandExecutor {
                    override fun execute(sender: GlobalSender, parents: Array<String>, args: Array<String>): Boolean {
                        if (args.isEmpty()) {
                            infoCommand.execute(sender, arrayOf(*parents, "info"), arrayOf())
                            return true
                        }
                        return false
                    }
                })
                .subCommand(runCommand)
                .subCommand(evaluateCommand)
                .subCommand(reloadCommand)
                .subCommand(infoCommand)
                .build()
            subCommands.add(subCommand)
        }
    }


}
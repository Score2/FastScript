package me.scoretwo.fastscript.api.expansion

import me.scoretwo.fastscript.api.script.Script
import me.scoretwo.utils.sender.GlobalSender

abstract class FastScriptExpansion {

    abstract val name: String
    abstract val sign: String
    abstract val fileSuffix: String

    @Deprecated("已不需要该 boolean 进行判断, 脚本不应该在每次 execute 时进行 evaluate")
    abstract val needEval: Boolean

    abstract fun reload(): FastScriptExpansion

    abstract fun eval(text: String, sender: GlobalSender, args: Array<Any?> = arrayOf(), otherBindings: Map<String, Any?> = mutableMapOf()): Any?
    abstract fun eval(script: Script, sender: GlobalSender, args: Array<Any?> = arrayOf(), otherBindings: Map<String, Any?> = mutableMapOf()): Any?
    abstract fun execute(script: Script, sender: GlobalSender, main: String = script.option.main, args: Array<Any?> = arrayOf(), otherBindings: Map<String, Any?> = mutableMapOf()): Any?
    abstract fun execute(text: String, sender: GlobalSender, main: String = "main", args: Array<Any?> = arrayOf(), otherBindings: Map<String, Any?> = mutableMapOf()): Any?

    // abstract fun eval(script: Script, sender: GlobalSender): Any?

    // abstract fun execute(script: Script, sender: GlobalSender, main: String, args: Array<Any?>): Any?
}
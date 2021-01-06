package me.scoretwo.fastscript.config

import me.scoretwo.utils.bukkit.configuration.yaml.file.YamlConfiguration
import java.io.File

abstract class Config: YamlConfiguration {

    val file: File

    constructor(file: File) {
        this.file = file
        this.load(file)
    }

    abstract fun onReload()

}
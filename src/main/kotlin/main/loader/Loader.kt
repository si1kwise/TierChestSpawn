package main.loader

import org.bukkit.plugin.java.JavaPlugin

class Loader : JavaPlugin() {

    private val commandRegistrationHandler = CommandRegistrationHandler(this)

    override fun onEnable() {
        saveDefaultConfig()
        reloadConfig()

        commandRegistrationHandler.registerCommandExecutors()
    }
}
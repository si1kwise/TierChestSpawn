package main.loader

import main.command.abstractcommand.AbstractCommand
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import org.reflections.util.ClasspathHelper

class CommandRegistrationHandler(
    private val javaPlugin: JavaPlugin
) {

    fun registerCommandExecutors() {
        for (command in Reflections(ClasspathHelper.forPackage("commandexecutor")).getSubTypesOf(AbstractCommand::class.java)) {
            val abstractCommand: AbstractCommand = command.getConstructor(JavaPlugin::class.java).newInstance(javaPlugin)

            javaPlugin.getCommand(abstractCommand.commandInfo.name)?.setExecutor(abstractCommand)
        }
    }
}
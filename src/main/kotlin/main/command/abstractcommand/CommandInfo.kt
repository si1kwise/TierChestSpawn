package main.command.abstractcommand

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandInfo(
    val name: String,
    val permissions: String = "",
    val playerRequired: Boolean = false
)

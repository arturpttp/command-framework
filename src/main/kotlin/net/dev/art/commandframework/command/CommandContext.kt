package net.dev.art.commandframework.command

data class CommandContext(
    val aliases: Array<String>,
    val permission: String,
    val description: String,
    private val commandPrefix: String = "/",
    var label: String = "fakeLabel",
    var command: ICommand? = null,
    val usage: String = commandPrefix + command?.invoke
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CommandContext

        if (!aliases.contentEquals(other.aliases)) return false
        if (permission != other.permission) return false
        if (description != other.description) return false
        if (commandPrefix != other.commandPrefix) return false
        if (command != other.command) return false
        if (label != other.label) return false
        if (usage != other.usage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = aliases.contentHashCode()
        result = 31 * result + permission.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + commandPrefix.hashCode()
        result = 31 * result + command.hashCode()
        result = 31 * result + label.hashCode()
        result = 31 * result + usage.hashCode()
        return result
    }

}
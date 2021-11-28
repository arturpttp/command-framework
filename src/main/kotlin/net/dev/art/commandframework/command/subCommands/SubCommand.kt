package net.dev.art.commandframework.command.subCommands

import net.dev.art.commandframework.command.ICommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

abstract class SubCommand(
    override val name: String,
    override val aliases: Array<String>,
    override val permission: String = "$name.use",
    override val usage: String = name
) : ISubCommand {

    override lateinit var parent: ICommand
    override lateinit var sender: CommandSender

    override fun beforeExecute(parent: ICommand) {
        this.sender = parent.sender
    }

    override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
        return false
    }

    override fun execute(player: Player, args: Array<out String>): Boolean {
        return false
    }

    override fun check(sender: CommandSender): Boolean {
        return !(!sender.hasPermission(permission) || (parent.onlyPlayers && sender !is Player))
    }

    fun sendMessage(message: String) {
        sender.sendMessage(message)
    }

    override fun register(parent: ICommand) {
        this.parent = parent
        this.parent.registerSubCommand(this)
    }
}
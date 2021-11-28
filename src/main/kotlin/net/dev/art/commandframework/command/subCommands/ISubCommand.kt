package net.dev.art.commandframework.command.subCommands

import net.dev.art.commandframework.command.ICommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

interface ISubCommand {

    var parent: ICommand
    val name: String
    val aliases: Array<String>

    val usage: String
    val permission: String

    var sender: CommandSender

    fun beforeExecute(parent: ICommand)

    fun execute(sender: CommandSender, args: Array<out String>): Boolean
    fun execute(player: Player, args: Array<out String>): Boolean

    fun check(sender: CommandSender): Boolean

    fun register(parent: ICommand)


}
package net.dev.art.commandframework.commands

import net.dev.art.commandframework.CommandFramework
import net.dev.art.commandframework.command.Command
import net.dev.art.commandframework.command.CommandContext
import net.dev.art.commandframework.commands.subs.TestConsoleSubCommand
import net.dev.art.core.Core
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TestCommand : Command(
    "test",
    CommandFramework.instance,
    context = CommandContext(
        arrayOf("tcommand"),
        "test.usage",
        "Test command for help-me"
    ),
    onlyPlayers = false
) {

    init {
        TestConsoleSubCommand().register(this)
    }

    override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
        sendMessage("You're using label; $label")
//        if (args.isNotEmpty()) sendMessage(args[0])
        return true
    }

    override fun execute(player: Player, args: Array<out String>): Boolean {
        return false
    }

}
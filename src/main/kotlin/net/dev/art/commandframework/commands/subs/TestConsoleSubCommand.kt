package net.dev.art.commandframework.commands.subs

import net.dev.art.commandframework.command.subCommands.SubCommand
import net.dev.art.commandframework.commands.TestCommand
import org.bukkit.command.CommandSender

class TestConsoleSubCommand(): SubCommand(
    "console",
    arrayOf("tconsole"),
) {

    override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
        sendMessage("this is the console sub command executed by using `/${parent.invoke} $name`")
        return false
    }

}
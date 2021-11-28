package net.dev.art.commandframework

import net.dev.art.commandframework.command.Command
import net.dev.art.commandframework.command.InlineCommand
import net.dev.art.core.libs.PluginCore
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandFramework : PluginCore("Command Framework", CommandFramework::class.java) {

    companion object {
        lateinit var instance: CommandFramework
    }

    init {
        instance = this
        registries.add(Command::class.java)
        commands()
    }

    private fun commands() {



        /*
        registerInlineCommand(object : InlineCommand("editItem", onlyPlayers = true) {
            override fun execute(player: Player, args: Array<out String>): Boolean {

                return true
            }
        })
        registerInlineCommand(object : InlineCommand("itemInfo", onlyPlayers = true) {
            override fun execute(player: Player, args: Array<out String>): Boolean {
                if (player.itemInHand != null && player.itemInHand.type != Material.AIR) {
                    val item = player.itemInHand
                    player.sendMessages(
                        "&e------------------> &7Item Information&e <-----------------",
                        "&e-> &7Name: &r ${if (item.itemMeta.hasDisplayName()) item.itemMeta.displayName.color() else item.type.name}",
                        "&e-> &7Lore: &r ${if (!item.itemMeta.hasLore()) "&cLore not found" else ""}"
                    )
                    if (item.itemMeta.hasLore())
                        item.itemMeta.lore.forEach {
                            player.sendMessage("&8   * &r ${it.color()}")
                        }
                    player.sendMessages(
                        "&e-> Id: &r ${item.type.id}",
                        "&e-> Data: &r ${item.durability}",
                        "&e------------------------------------------------------------"
                    )
                } else {
                    player.sendMessage("&cYour hand is empty, hold the item that you want the information".color())
                }
                return true
            }
        }.enableDelay(3))
         */
    }

}

var PluginCore.enableInlineCommands: Boolean
    get() = true
    set(value) {}

fun PluginCore.registerInlineCommand(inlineCommand: InlineCommand) {
    if (this.enableInlineCommands) {
        inlineCommand.register(this)
    } else
        throw IllegalAccessError("You try to register a inline command when ${this.name} doesn't accept inline commands!")
}
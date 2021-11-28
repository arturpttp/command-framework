package net.dev.art.commandframework.command

import net.dev.art.commandframework.command.subCommands.ISubCommand
import net.dev.art.core.libs.interfaces.AutoRegistrable
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_8_R3.CraftServer
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.lang.reflect.Field

interface ICommand {

    val invoke: String
    val context: CommandContext
    val plugin: Plugin
    var sender: CommandSender
    val autoCheckPermission: Boolean
    val onlyPlayers: Boolean

    val subCommands: MutableList<ISubCommand>
    var inDelayPlayers: MutableSet<Player>
    var delay: Int

    val noPermissionMessage: String
    val onlyPlayersMessage: String
    val inDelayMessage: String

    fun execute(sender: CommandSender, args: Array<out String>): Boolean

    fun execute(player: Player, args: Array<out String>): Boolean

    fun getCommandMap(): CommandMap? {
        return try {
            if (Bukkit.getServer() is CraftServer) {
                val field: Field = CraftServer::class.java.getDeclaredField("commandMap")
                field.isAccessible = true
                return field[Bukkit.getServer()] as CommandMap
            }
            null
        } catch (e: SecurityException) {
            e.printStackTrace()
            null
        }
    }

    fun registerSubCommand(subCommand: ISubCommand)

}
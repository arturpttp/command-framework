package net.dev.art.commandframework.command

import net.dev.art.commandframework.CommandFramework
import net.dev.art.commandframework.command.subCommands.ISubCommand
import net.dev.art.core.libs.PluginCore
import net.dev.art.core.libs.color
import net.dev.art.core.libs.console
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

/*Created in 07:20 at 08/11/2021 in TestCommand.kt By artur*/
abstract class InlineCommand(
    override var invoke: String = "fakeCommand",
    var _usage: String = "",
    var _permission: String = "",
    var _description: String = "",
    var _aliases: Array<String> = arrayOf(),
    override val plugin: Plugin = CommandFramework.instance,
    override val autoCheckPermission: Boolean = true,
    override var onlyPlayers: Boolean = false,
    override var delay: Int = -1,
    override var inDelayPlayers: MutableSet<Player> = mutableSetOf()
) : org.bukkit.command.Command(invoke), ICommand {


    /**
     * This class doesn't support sub commands
     */
    override val subCommands: MutableList<ISubCommand> = mutableListOf()
    override fun registerSubCommand(subCommand: ISubCommand) {}

    final override val context: CommandContext = CommandContext(_aliases, _permission, _description, usage = _usage)
    override lateinit var sender: CommandSender
    override val noPermissionMessage: String = "&cYou don't have permission".color()
    override val onlyPlayersMessage: String = "&cCommand only for players! Your strange!!!".color()
    override val inDelayMessage: String = "&cWait a few seconds until you can use it again!".color()

    init {
        context.command = this
    }

    private var registry = true

    fun register(plugin: PluginCore) {
        registry = !invoke.equals("fakeCommand", true)
        if (registry) {
            if (getCommandMap() == null) {
                console("&aCommandAPI &f-> &4ERROR: &cCouldn't find CommandMap, contact plugin creator or change your SpigotApi version!".color())
                return
            }
            this.description = context.description
            this.aliases = context.aliases.toMutableList()
            this.usageMessage = context.usage
            this.permission = context.permission
            getCommandMap()?.register(plugin.name.lowercase().replace(" ", "_"), this)
            console("§aCommandAPI §f-> &eLoading command §f$name...".color())
        }
    }

    override fun execute(player: Player, args: Array<out String>): Boolean = false

    override fun execute(sender: CommandSender, args: Array<out String>): Boolean = false

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        this.sender = sender
        this.context.label = label
        return commandExecute(sender, args)
    }

    fun enableDelay(delay: Int): InlineCommand {
        this.delay = delay
        this.inDelayPlayers = mutableSetOf()
        return this
    }

    private fun commandExecute(sender: CommandSender, args: Array<out String>): Boolean {
        if (check()) {
            var ret = execute(sender, args)
            if (sender is Player)
                ret = execute(senderAsPlayer(), args)
            return ret
        }
        return false
    }

    private fun senderAsPlayer(): Player {
        return (sender as Player)
    }

    private fun check(): Boolean {
        if (autoCheckPermission && !sender.hasPermission(context.permission)) {
            sender.sendMessage(noPermissionMessage)
            return false
        }
        if (onlyPlayers && (sender !is Player)) {
            sender.sendMessage(onlyPlayersMessage)
            return false
        }
        if (sender is Player && delay > 0) {
            if (inDelayPlayers.contains(senderAsPlayer())) {
                sender.sendMessage(inDelayMessage)
                return false
            }
            inDelayPlayers.add(sender as Player)
            Bukkit.getScheduler().runTaskLaterAsynchronously(
                plugin,
                object : BukkitRunnable() {
                    override fun run() {
                        if (inDelayPlayers.contains(senderAsPlayer()))
                            inDelayPlayers.remove(senderAsPlayer())
                    }
                },
                delay * 20L
            )
        }
        return true
    }

}
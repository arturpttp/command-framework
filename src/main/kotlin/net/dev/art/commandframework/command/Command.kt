package net.dev.art.commandframework.command

import net.dev.art.commandframework.CommandFramework
import net.dev.art.commandframework.command.subCommands.ISubCommand
import net.dev.art.core.libs.PluginCore
import net.dev.art.core.libs.color
import net.dev.art.core.libs.console
import net.dev.art.core.libs.interfaces.AutoRegistrable
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

open class Command(
    override val invoke: String = "fakeCommand",
    override val plugin: Plugin = CommandFramework.instance,
    private val _usage: String = "",
    private val _permission: String = "",
    private val _description: String = "",
    private val _aliases: Array<String> = arrayOf(),
    override val autoCheckPermission: Boolean = true,
    override val onlyPlayers: Boolean = false,
    override var delay: Int = -1,
    override val subCommands: MutableList<ISubCommand> = mutableListOf(),
    override val context: CommandContext = CommandContext(_aliases, _permission, _description, usage = _usage)
) : org.bukkit.command.Command(invoke), ICommand, AutoRegistrable, SupportSubCommands {

    override val type: Class<*> = Command::class.java
    override val loadMessage: String = "§aCommandAPI §f-> §eInitializing..."
    override val notMatches: MutableList<Class<*>> = mutableListOf(
        Command::class.java
    )

    override lateinit var sender: CommandSender
    override var inDelayPlayers: MutableSet<Player> = mutableSetOf()


    override val noPermissionMessage: String = "&cYou don't have permission".color()
    override val onlyPlayersMessage: String = "&cCommand only for players! Your strange!!!".color()
    override val inDelayMessage: String = "&cWait a few seconds until you can use it again!".color()

    private var registry = true

    override fun register(plugin: PluginCore) {
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
//            super.register(getCommandMap())
            console("§aCommandAPI §f-> &eLoading command §f$name...".color())
        }
    }

    fun enableDelay(delay: Int) {
        this.delay = delay
        this.inDelayPlayers = mutableSetOf()
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

    init {
        context.command = this
    }

    protected fun senderAsPlayer(): Player {
        return (sender as Player)
    }

    fun sendMessage(vararg message: String) {
        sender.sendMessage(message)
    }

    override fun registerSubCommand(subCommand: ISubCommand) {
        subCommands.add(subCommand)
    }

    override fun execute(player: Player, args: Array<out String>): Boolean {
        return false
    }

    override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
        return false
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        this.sender = sender
        this.context.label = label
        if (args.isNotEmpty() && subCommands.isNotEmpty() && subCommands.contains(args[0])) {
            val sc = args[0]
            val subCommand = subCommands.get(sc)
            subCommand.beforeExecute(this)
            return if (subCommand.check(sender)) {
                if (onlyPlayers) subCommand.execute(senderAsPlayer(), args) else {
                    var ret = subCommand.execute(sender, args)
                    if (sender is Player)
                        ret = subCommand.execute(senderAsPlayer(), args)
                    ret
                }
            } else false
        }
        return commandExecute(sender, args)
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

    fun MutableList<ISubCommand>.contains(name: String): Boolean =
        this.firstOrNull { it.name.equals(name, true) || it.aliases.contains(name) } != null

    fun MutableList<ISubCommand>.get(name: String): ISubCommand =
        this.first { it.name.equals(name, true) || it.aliases.contains(name) }

}
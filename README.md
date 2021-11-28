# Command Framework
<img align="right" src="https://media.discordapp.net/attachments/517334221660880907/908178316572377168/ezgif-2-8ced1ea5b417.gif" height="170" width="170" title="a">

A command framework made in kotlin with **[Core](https://github.com/arturpttp/core)**, command framework depends on it, you should have it in mind.
<br>
_Please see the **[Core](https://github.com/arturpttp/core)** for more information about minecraft plugins._
## Creating your first command

To create your first command, you have many ways, to simple command you could do it: 

**Simple Command**

```kotlin
//You've to use a CorePlugin main to do this
pluginCoreInstance.registerInlineCommand(object : InlineCommand("test") {
    init {
        _usage = "/test arg1"
        onlyPlayers = false
        _permission = "test.test"
        _aliases = arrayOf("test1")
        _description = "desc"
    }

    //When it's a player that execute it
    override fun execute(player: Player, args: Array<out String>): Boolean {
        return true
    }

    //When it's the console that execute it
    override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
        return true
    }
})
```

**Commands in separated classes**

```kotlin
class TestCommand : Command(
    "test", //Your command invoke
    Plugin.instance, //Your PluginCore/Main class
    context = CommandContext(
        arrayOf("tcommand"),
        "test.usage",
        "Test command for help-me"
    ),
    onlyPlayers = false
) {

    init {
        TestConsoleSubCommand().register(this) //Commands in separated classes has access to subComamnds, inline commands not.
    }

    override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
        sendMessage("You're using label; $label")
        return true
    }

    override fun execute(player: Player, args: Array<out String>): Boolean {
        return false
    }

}
``` 
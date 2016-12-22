package com.spbau.wimag.cli.command

import com.spbau.wimag.cli.command.commands.*
import com.spbau.wimag.cli.envireronment.Database
import com.spbau.wimag.cli.parser.Call

/**
 * Factory for creating commands
 * from Call Token
 */
object CallCommandFactory {

    fun forCommandCall(call: Call): Command {
        val command = call.executable.toString { Database.get(it) }
        val firstInput = call.params.firstOrNull()?.toString { Database.get(it) }
        return when (command) {
            "cat" -> {
                if (firstInput != null) {
                    CatCommand(firstInput)
                } else {
                    CatCommand()
                }
            }
            "echo" -> EchoCommand(call)
            "wc" -> {
                if (firstInput != null) {
                    WcCommand(firstInput)
                } else {
                    WcCommand()
                }
            }
            "pwd" -> PwdCommand()
            "exit" -> ExitCommand()
            "grep" -> GrepCommand(call)
            else -> ExternalCommand(call)
        }
    }
}
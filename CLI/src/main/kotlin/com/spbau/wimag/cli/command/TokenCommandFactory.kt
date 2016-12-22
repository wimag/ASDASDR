package com.spbau.wimag.cli.command

import com.spbau.wimag.cli.command.commands.AssignCommand
import com.spbau.wimag.cli.command.commands.Command
import com.spbau.wimag.cli.command.commands.EmptyCommand
import com.spbau.wimag.cli.parser.Assign
import com.spbau.wimag.cli.parser.Call
import com.spbau.wimag.cli.parser.Empty
import com.spbau.wimag.cli.parser.TopLevelToken

/**
 * Factory for top level Token parsing
 */
object TokenCommandFactory {
    fun createCommandFor(token: TopLevelToken): Command {
        return when (token) {
            is Assign -> AssignCommand(token)
            is Empty -> EmptyCommand()
            is Call -> CallCommandFactory.forCommandCall(token)
            else -> throw CommandFormatException()
        }
    }
}
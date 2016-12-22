package com.spbau.wimag.cli.command

import com.spbau.wimag.cli.command.commands.Command
import com.spbau.wimag.cli.command.commands.CompositeCommand
import com.spbau.wimag.cli.command.commands.EmptyCommand
import com.spbau.wimag.cli.parser.TopLevelToken

/**
 * Builder that helps to build Command from parser output
 */
class CommandBuilder {
    private var commands = mutableListOf<Command>()

    /**
     * Add a top level token to
     */
    fun add(token: TopLevelToken): CommandBuilder {
        commands.add(TokenCommandFactory.createCommandFor(token))
        return this
    }

    /**
     * Add a list of top level tokens to parsed command
     */
    fun addAll(tokens: Collection<TopLevelToken>): CommandBuilder {
        commands.addAll(tokens.map { TokenCommandFactory.createCommandFor(it) })
        return this
    }

    /**
     * Finalize command
     */
    fun build(): Command {
        if (commands.size == 0) {
            return EmptyCommand()
        }
        val res: Command
        if (commands.size == 1) {
            res = commands[0]
        } else {
            res = CompositeCommand(commands)
        }
        commands = mutableListOf<Command>()
        return res
    }
}
package com.spbau.wimag.cli

import com.spbau.wimag.cli.command.CommandBuilder
import com.spbau.wimag.cli.command.CommandFormatException
import com.spbau.wimag.cli.parser.Parser
import jdk.nashorn.internal.runtime.ParserException

fun main(args: Array<String>) {
    while (true) {
        val line = readLine() ?: break
        try {
            CommandBuilder().addAll(Parser.parse(line)).build().execute()
        } catch (e: ParserException) {
            println("Can not parse command, try again")
        } catch (e: CommandFormatException) {
            println("Malformed command, please try agains")
        }

    }
}
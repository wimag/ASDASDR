package com.spbau.wimag.cli.command.commands

import com.spbau.wimag.cli.command.CommandFormatException
import com.spbau.wimag.cli.command.utils.CommandUtils
import com.spbau.wimag.cli.envireronment.Database
import com.spbau.wimag.cli.parser.Call
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * Class represents external command (e.g not default implemented)
 */
class ExternalCommand(val command: Call) : Command() {

    /**
     * Call command via process builder
     */
    override fun execute(input: InputStream, output: OutputStream) {
        val formattedArgs = command.params.map { x -> x.toString { Database.get(it) } }.map { "'$it'" }
        val formattedCommand: String = command.executable.toString { Database.get(it) }
        val args = mutableListOf(formattedCommand)
        args.addAll(formattedArgs)
        try {
            val process = ProcessBuilder(args).start()
            if (input != System.`in`) {
                CommandUtils.pipe(input, process.outputStream)
            }
            CommandUtils.pipe(process.inputStream, output)
        } catch (e: IOException) {
            throw CommandFormatException()
        }
    }
}
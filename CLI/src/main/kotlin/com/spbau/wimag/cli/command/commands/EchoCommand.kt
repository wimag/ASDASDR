package com.spbau.wimag.cli.command.commands

import com.spbau.wimag.cli.envireronment.Database
import com.spbau.wimag.cli.parser.Call
import java.io.InputStream
import java.io.OutputStream


/**
 * Echo command - reads from input stream, writes to output
 */
class EchoCommand(val command: Call) : Command() {


    override fun execute(input: InputStream, output: OutputStream) {
        val args = command.params.map { it.toString { Database.get(it) } }.joinToString(" ") + "\n"
        output.write(args.toByteArray())
    }
}
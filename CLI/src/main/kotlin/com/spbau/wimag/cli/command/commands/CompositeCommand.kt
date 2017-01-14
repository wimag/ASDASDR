package com.spbau.wimag.cli.command.commands

import java.io.InputStream
import java.io.OutputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream


/**
 * Class for execution of piped commands
 */
class CompositeCommand(val commands: Collection<Command>) : Command() {

    /**
     * See {@link Command}
     */
    override fun execute(input: InputStream, output: OutputStream) {
        val outputs = Array(commands.size - 1) { PipedOutputStream() }
        val inputs = Array(commands.size - 1) { PipedInputStream(outputs[it]) }
        val tmp = commands.toList()
        tmp[0].execute(input, outputs[0])
        outputs[0].close()
        for (i in 1..tmp.size - 2) {
            tmp[i].execute(inputs[i - 1], outputs[i])
            inputs[i - 1].close()
            outputs[i].close()
        }
        tmp.last().execute(inputs.last(), output)
        inputs.last().close()
    }
}
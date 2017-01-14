package com.spbau.wimag.cli.command.commands

import java.io.InputStream
import java.io.OutputStream

/**
 * literally command that does nothing
 */
class EmptyCommand : Command() {

    /**
     * See {@link Command}
     */
    override fun execute(input: InputStream, output: OutputStream) {
    }
}
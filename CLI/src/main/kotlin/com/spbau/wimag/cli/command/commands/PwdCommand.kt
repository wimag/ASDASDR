package com.spbau.wimag.cli.command.commands

import java.io.InputStream
import java.io.OutputStream

/**
 * Command that prints current directory
 */
class PwdCommand : Command() {

    /**
     * Print current system directory
     * See {@link Command}
     */
    override fun execute(input: InputStream, output: OutputStream) {
        output.write(System.getProperty("user.dir").toByteArray())
    }
}
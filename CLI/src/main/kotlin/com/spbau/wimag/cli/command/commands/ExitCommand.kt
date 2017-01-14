package com.spbau.wimag.cli.command.commands

import java.io.InputStream
import java.io.OutputStream

/**
 * Force quit
 */
class ExitCommand : Command() {
    override fun execute(input: InputStream, output: OutputStream) {
        System.exit(0)
    }
}
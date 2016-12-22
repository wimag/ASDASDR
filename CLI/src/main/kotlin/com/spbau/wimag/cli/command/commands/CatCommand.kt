package com.spbau.wimag.cli.command.commands

import java.io.*

/**
 * Class describes cat command
 */
class CatCommand(val input: InputStream? = null) : Command() {
    constructor(filename: String) : this(FileInputStream(filename))

    /**
     * See {@link Command}
     */
    override fun execute(input: InputStream, output: OutputStream) {
        val stream = this.input ?: input
        val reader = BufferedReader(InputStreamReader(stream))
        reader.forEachLine { output.write((it + "\n").toByteArray()) }
        output.flush()
    }
}

package com.spbau.wimag.cli.command.commands

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Wc command implementation
 */
class WcCommand(val input: InputStream? = null) : Command() {
    constructor(filename: String) : this(FileInputStream(filename))

    /**
     * Count words|line|bytes in stream
     * See {@link Command}
     */
    override fun execute(input: InputStream, output: OutputStream) {
        val stream = this.input ?: input
        val temp = File.createTempFile("wc-input", ".tmp")
        val path = Paths.get(temp.absolutePath)
        temp.delete()
        Files.copy(stream, path)
        val bytes = Files.size(path)
        val words = Files.lines(path).map { x -> x.split(" ").size }.toArray().sumBy { x -> x as Int }
        val lines = Files.lines(path).count()
        val res = "$lines $words $bytes"
        output.write(res.toByteArray())
        Files.delete(path)
    }
}
package com.spbau.wimag.cli.command.utils

import java.io.InputStream
import java.io.OutputStream

/**
 * Helpers for commands
 */
object CommandUtils {

    /**
     * Pipes everything from input stream to output stream
     */
    fun pipe(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(1024)
        var bytesRead: Int
        bytesRead = input.read(buffer)
        while (bytesRead != -1) {
            output.write(buffer, 0, bytesRead)
            bytesRead = input.read(buffer)
        }
    }
}
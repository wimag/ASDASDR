package com.spbau.wimag.cli.command.commands

import com.spbau.wimag.cli.envireronment.Database
import com.spbau.wimag.cli.parser.Assign
import java.io.InputStream
import java.io.OutputStream

/**
 * Represents assign expression.
 * Reads nothing, outputs nothing
 */

class AssignCommand(token: Assign) : Command() {
    val varName = token.left
    var value = token.right.toString { Database.get(it) }

    override fun execute(input: InputStream, output: OutputStream) {
        Database.set(varName, value)
    }
}
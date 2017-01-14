package com.spbau.wimag.cli.command.commands

import com.spbau.wimag.cli.command.CommandFormatException
import com.spbau.wimag.cli.envireronment.Database
import com.spbau.wimag.cli.parser.Call
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.GnuParser
import org.apache.commons.cli.Options
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

/**
 * Represents grep command - search substrings in files
 */
class GrepCommand(command: Call) : Command(){
    val args = command.params.map { it.toString { Database.get(it) } }.toTypedArray()

    /**
     * Execute grep command.
     * Program takes at least on argument - regex to find
     */
    override fun execute(input: InputStream, output: OutputStream) {
        val options = Options()

        options.addOption("i", false, "make search case insensitive")
        options.addOption("w", false, "whole words only")
        options.addOption("A", true, "print lines after match")

        val parser = GnuParser()
        val cmd : CommandLine = parser.parse(options, args)

        val actualArgs = mutableListOf<String>(*cmd.args)

        if (actualArgs.isEmpty()){
            throw CommandFormatException()
        }

        if(input != System.`in`){
            val temp = File.createTempFile("grep", ".tmp")
            val path = Paths.get(temp.absolutePath)
            temp.delete()
            Files.copy(input, path)
            actualArgs.add(path.toString())
        }

        if (actualArgs.size < 2){
            throw CommandFormatException()
        }

        val pattern = actualArgs[0]
        val regex = compileRegex(pattern, cmd.hasOption("i"), cmd.hasOption("w"))

        val extraLines = cmd.getOptionValue("A")?.toInt() ?: 1

        var linesLeftToPrint: Int = 0
        for (filename in actualArgs.drop(1)){
            try {
                val lines = Files.lines(Paths.get(filename)).collect(Collectors.toList<String>())
                for (line in lines){
                    if (line.contains(regex)){
                        linesLeftToPrint = extraLines
                    }
                    if (linesLeftToPrint > 0){
                        output.write((line + "\n").toByteArray())
                        linesLeftToPrint --
                    }
                }
            } catch (e: IOException){
                throw CommandFormatException()
            }
        }
    }

    /**
     * Generate regex object based on provided properties
     * @param initialRegex - user provided regex
     * @param ignoreCase - whether to ignore case
     * @param wholeWords - whether to search for whole words only
     * @return Regex matching input
     */
    private fun compileRegex(initialRegex: String, ignoreCase: Boolean, wholeWords: Boolean) : Regex{
        var regexString = initialRegex
        if (wholeWords) {
            regexString = "(\\b+)$regexString(\\s+|$)"
        }
        if(ignoreCase) {
            return Regex(regexString, RegexOption.IGNORE_CASE)
        } else {
            return Regex(regexString)
        }
    }

}
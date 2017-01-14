package com.spbau.wimag.cli.command

/**
 * Exception for malformed command
 */
class CommandFormatException : IllegalArgumentException("Can not create command from arguments")
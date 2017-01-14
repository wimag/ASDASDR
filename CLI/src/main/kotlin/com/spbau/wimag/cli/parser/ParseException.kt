package com.spbau.wimag.cli.parser

/**
 * Exception raised in case of Parser failure
 * on input string param
 */
class ParseException(param: String) : IllegalArgumentException("Cannot parse string" + param)
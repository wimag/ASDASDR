package com.spbau.wimag.cli.parser


/**
 * Prototype for results of string parsing
 */
interface ParseToken

/**
 * Tokens representing top level entities like calls or assignmens
 */
interface TopLevelToken : ParseToken

/**
 * Tokens for weak/strong quoted strings
 */
interface StringToken : ParseToken {
    /**
     * Inline variables and convert to string
     */
    fun toString(resolver: (String) -> (String)): String
}

/**
 * Represents assign operation: 'var=value'
 * @param left - variable name
 * @param right - what to assign
 */
data class Assign(val left: String, val right: StringToken) : TopLevelToken

/**
 * Represents call operation 'fun arg1 arg2 arg3'
 * @param executable - command name
 * @param params - command arguments
 */
data class Call(val executable: StringToken, val params: List<StringToken>) : TopLevelToken

/**
 * Represents weak quoted string, represented as
 * string parts separated by variable identifiers
 *
 * pair of lists : string parts sp
 * and variable parts vp: string consist of
 * (sp@1, vp@1, sp@2, vp@2...sp@n)
 * @param stringParts - string literals
 * @param separatorVars - variables, that separate {WeakString#stringParts}
 */
data class WeakString(private val stringParts: List<String>, private val separatorVars: List<String>) : StringToken {
    init {
        if (stringParts.size != separatorVars.size + 1) {
            throw ParseException(stringParts.joinToString(" ") + separatorVars.joinToString(" "))
        }
    }

    /**
     * given resolver - inline variables and build string string
     * @param resolver - function to resolve inline variables
     */
    fun inlineVars(resolver: (String) -> (String)): StrongString {
        val sb = StringBuilder()
        sb.append(stringParts[0])
        for (i in 0..separatorVars.size - 1) {
            sb.append(resolver(separatorVars[i]))
            sb.append(stringParts[i + 1])
        }
        return StrongString(sb.toString())
    }

    override fun toString(resolver: (String) -> String): String {
        return inlineVars(resolver).value
    }
}

/**
 * represents strong quoted string
 * @param value - inner text
 */
data class StrongString(val value: String) : StringToken {
    override fun toString(): String {
        return value
    }

    override fun toString(resolver: (String) -> String): String {
        return value
    }
}

/**
 * Not yet parsed string
 */
data class Unparsed(val string: String) : ParseToken

/**
 * Do nothing token. e.g. ||
 */
class Empty : TopLevelToken
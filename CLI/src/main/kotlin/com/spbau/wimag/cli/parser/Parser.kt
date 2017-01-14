package com.spbau.wimag.cli.parser


/**
 * Parser class singleton object.
 */
object Parser {
    /**
     * Single task of parser is to take in a string
     * and return parsed result
     * @param line - input string
     * @return - parsed result
     */
    fun parse(line: String): List<TopLevelToken> {
        val splitByStrongQuotes = splitByStrongQuotes(line)
        val splitByWeakQuotes: List<ParseToken> = splitByStrongQuotes.map { x ->
            when (x) {
                is StrongString -> listOf<ParseToken>(x)
                is Unparsed -> splitByWeakQuotes(x.string)
                else -> throw ParseException(x.toString())
            }
        }.flatten()
        val rawCommands = splitByPipe(splitByWeakQuotes)
        return buildCommands(rawCommands)
    }

    /**
     * Split string by strong quotes
     * @param s - string to split
     */
    private fun splitByStrongQuotes(s: String): List<ParseToken> {
        val tmp = splitByQuotes(s, '\'')
        val res = mutableListOf<ParseToken>()
        for (x in tmp) {
            if (x.isQuote) {
                res.add(StrongString(x.content))
            } else {
                res.add(Unparsed(x.content))
            }
        }
        return res
    }

    /**
     * Split string by strong quotes
     * @param s - string to split
     */
    private fun splitByWeakQuotes(s: String): List<ParseToken> {
        val tmp = splitByQuotes(s, '\"')
        val res = mutableListOf<ParseToken>()
        for (x in tmp) {
            if (x.isQuote) {
                res.add(parseInlineVars(x.content))
            } else {
                res.add(Unparsed(x.content))
            }
        }
        return res
    }

    /**
     * Given a weak string parse out varaibles
     * to inline.
     * @param s - input weak quoted string content
     * @return - WeakStringToken from s
     */
    private fun parseInlineVars(s: String): WeakString {
        val sp = mutableListOf<String>()
        val vp = mutableListOf<String>()
        val split = s.split('$')
        sp.add(split[0])
        for (i in 1..split.size - 1) {
            val words = split[i].split(Regex("\\s+"), 2)
            vp.add(words[0])
            if (words.size > 1) {
                sp.add(words[1])
            } else {
                sp.add("")
            }
        }
        return WeakString(sp, vp)
    }

    /**
     * Given sequence of unparsed and quoted strings
     * split them by pipe symbol
     *
     */
    private fun splitByPipe(seq: List<ParseToken>): List<List<ParseToken>> {
        val res = mutableListOf<List<ParseToken>>()
        var current = mutableListOf<ParseToken>()
        for (token in seq) {
            if (token is Unparsed) {
                val split = token.string.split("|")
                for (i in 0..split.size - 2) {
                    current.add(Unparsed(split[i]))
                    res.add(current)
                    current = mutableListOf()
                }
                current.add(Unparsed(split.last()))
            } else {
                current.add(token)
            }
        }
        if (current.isNotEmpty()) {
            res.add(current)
        }
        return res
    }

    /**
     * Split string by given quote type
     * @param s - string to splt
     * @param quote - character representring quote to split
     * @return list of ParseToken - either Strong string or unparsed
     */
    private fun splitByQuotes(s: String, quote: Char): List<QuoteParseResult> {
        var pos: Int = 0
        var openPos: Int = 0
        var opened: Boolean = false
        val res = mutableListOf<QuoteParseResult>()
        while (pos < s.length) {
            if (s[pos] == '\\') {
                pos += 2
                continue
            }
            if (s[pos] == quote) {
                val tmp = s.substring(openPos, pos)
                if (tmp.isNotEmpty()) {
                    res.add(QuoteParseResult(tmp, opened))
                }
                opened = !opened
                openPos = pos + 1
            }
            pos++
        }
        if (opened) {
            throw ParseException(s)
        }
        val tmp = s.substring(openPos, s.length)
        if (tmp.isNotEmpty()) {
            res.add(QuoteParseResult(tmp, false))
        }
        return res
    }

    /**
     * Build top level commands - either assignment or call
     */
    private fun buildCommands(seqs: List<List<ParseToken>>): List<TopLevelToken> {
        val res = mutableListOf<TopLevelToken>()
        for (seq in seqs) {
            val first = seq.firstOrNull()
            if (first == null || first !is Unparsed) { // If Sequence is empty or starts with quote - statement has no effect
                res.add(Empty())
                continue
            }
            val words = first.string.split(Regex("\\s+"))
            if (words[0].contains('=')) { // starts with assigment
                val firstWordSplit = words[0].split('=')
                val varname = firstWordSplit[0]
                val argument: StringToken
                if (firstWordSplit[1].isNotEmpty()) {
                    argument = parseStringOrVar(firstWordSplit[1])
                } else {
                    if (words.size == 1 && seq.size != 1 && seq[1] !is Unparsed) { // either x=value or x='val' || x="val". spaces are forbidden
                        if (seq[1] !is StringToken) {
                            throw ParseException(seq[1].toString())
                        }
                        argument = seq[1] as StringToken
                    } else {
                        throw ParseException(words[0])
                    }
                }
                res.add(Assign(varname, argument))
            } else { // call function
                val temp = mutableListOf<StringToken>()
                for (token in seq) {
                    if (token is Unparsed) {
                        temp.addAll(parseArgumentString(token))
                    } else {
                        if (token !is StringToken) {
                            throw ParseException(token.toString())
                        }
                        temp.add(token)
                    }
                }
                res.add(Call(temp[0], temp.drop(1)))
            }
        }
        return res
    }


    private fun parseArgumentString(token: Unparsed): List<StringToken> {
        return token.string.split(Regex("\\s+")).filter(String::isNotEmpty).map { parseStringOrVar(it) }
    }

    /**
     * Given a string replace it with either WeakString
     * if it is variable reference or with StrongString otherwise
     */
    private fun parseStringOrVar(s: String): StringToken {
        if (s.startsWith('$')) {
            return WeakString(listOf("", ""), listOf(s.drop(1)))
        } else {
            return StrongString(s)
        }
    }

    /**
     * temporary structure for representing quote parses
     */
    private data class QuoteParseResult(val content: String, val isQuote: Boolean)
}
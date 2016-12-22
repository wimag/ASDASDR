package com.spbau.wimag.cli.parser

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Mark on 21.12.2016.
 */
class ParserTest {
    @Test
    fun testAssignmentParse() {
        val testString = "FILE=example.txt"
        assertEquals(listOf(Assign("FILE", StrongString("example.txt"))), Parser.parse(testString))
    }

    @Test(expected = ParseException::class)
    fun tsetAssignmentFail() {
        val testString = "FILE= example.txt"
        Parser.parse(testString)
    }

    @Test
    fun testPipeline() {
        val testString = "cat example.txt | wc"
        assertEquals(listOf(
                Call(StrongString("cat"), listOf(StrongString("example.txt"))),
                Call(StrongString("wc"), listOf())
        ), Parser.parse(testString))
    }

    @Test
    fun testVariableInline() {
        val testString1 = "echo \$FILE"
        val expected = listOf(Call(StrongString("echo"), listOf(WeakString(listOf("", ""), listOf("FILE")))))
        assertEquals(expected, Parser.parse(testString1))
        val testString2 = "echo \"\$FILE\""
        assertEquals(expected, Parser.parse(testString2))
    }
}
package com.spbau.wimag.cli.command.commands

import com.spbau.wimag.cli.command.CommandBuilder
import com.spbau.wimag.cli.envireronment.Database
import com.spbau.wimag.cli.parser.Parser
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.PipedInputStream
import java.io.PipedOutputStream

/**
 * Created by Mark on 22.12.2016.
 */
class CommandTest {

    @Test
    fun AssignTest() {
        val line = "x=123"
        CommandBuilder().addAll(Parser.parse(line)).build().execute()
        assertEquals("123", Database.get("x"))
    }

    @Test
    fun CatTest() {
        val filename = "src/test/resources/input.txt"
        val command = "cat " + filename
        val out = PipedOutputStream()
        val inp = PipedInputStream(out)
        CommandBuilder().addAll(Parser.parse(command)).build().execute(System.`in`, out)
        out.close()
        assertEquals(listOf("1 2 3", "3 4"), inp.bufferedReader().readLines())
    }

    @Test
    fun CompositeTest() {
        val filename = "src/test/resources/input.txt"
        val line = "cat $filename | wc"
        val out = PipedOutputStream()
        val inp = PipedInputStream(out)
        CommandBuilder().addAll(Parser.parse(line)).build().execute(System.`in`, out)
        out.close()
        assertEquals(listOf("2 5 10"), inp.bufferedReader().readLines())
    }

    @Test
    fun EchoTest() {
        val command = "echo '123'"
        val out = PipedOutputStream()
        val inp = PipedInputStream(out)
        CommandBuilder().addAll(Parser.parse(command)).build().execute(System.`in`, out)
        out.close()
        assertEquals(listOf("123"), inp.bufferedReader().readLines())
    }

    @Test
    fun PwdTest() {
        val command = "pwd"
        val out = PipedOutputStream()
        val inp = PipedInputStream(out)
        CommandBuilder().addAll(Parser.parse(command)).build().execute(System.`in`, out)
        out.close()
        assertEquals(System.getProperty("user.dir"), inp.bufferedReader().readLine())
    }

    @Test
    fun WcTest() {
        val filename = "src/test/resources/input.txt"
        val line = "wc $filename"
        val out = PipedOutputStream()
        val inp = PipedInputStream(out)
        CommandBuilder().addAll(Parser.parse(line)).build().execute(System.`in`, out)
        out.close()
        assertEquals(listOf("2 5 10"), inp.bufferedReader().readLines())
    }
}
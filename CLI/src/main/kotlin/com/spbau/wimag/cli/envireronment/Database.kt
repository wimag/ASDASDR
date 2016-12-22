package com.spbau.wimag.cli.envireronment

/**
 * Storage for environmental variables
 */
object Database {
    private val values = mutableMapOf<String, String>()

    fun get(name: String): String {
        return values[name] ?: ""
    }

    fun set(name: String, value: String) {
        values[name] = value
    }
}
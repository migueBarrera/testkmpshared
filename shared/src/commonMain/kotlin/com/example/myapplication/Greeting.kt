package com.example.myapplication

class Greeting {
    private val platform: Platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name} from KMP!"
    }

    /**
     * Suma dos números enteros.
     *
     * @param a El primer número.
     * @param b El segundo número.
     * @return La suma de `a` y `b`.
     */
    fun greetComment(): String {
        return "Hello, ${platform.name}!"
    }
}
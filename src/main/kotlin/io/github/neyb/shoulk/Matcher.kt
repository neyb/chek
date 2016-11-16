package io.github.neyb.shoulk

interface Matcher<in T> {
    companion object Factory

    fun match(actual: T): Boolean
    fun assertionErrorMessage(actual: T): String
}
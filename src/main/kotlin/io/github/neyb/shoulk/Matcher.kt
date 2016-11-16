package io.github.neyb.shoulk

interface Matcher<in T> {
    companion object Factory

    fun match(actual: T): Boolean
    fun getDismatchDescriptionFor(actual: T): String
}
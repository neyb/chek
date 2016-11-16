package io.github.neyb.shoulk

interface Matcher<in T> {
    fun match(actual: T): Boolean
    fun getDismatchDescriptionFor(actual: T): String
}
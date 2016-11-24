package io.github.neyb.shoulk.Matcher

interface Matcher<in T> {
    fun match(actual: T): Boolean
    fun getDismatchDescriptionFor(actual: T): String
}
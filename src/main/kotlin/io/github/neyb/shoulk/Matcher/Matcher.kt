package io.github.neyb.shoulk.Matcher

interface Matcher<in T> {
    val description:String
    fun match(actual: T): MatchResult
    fun getDismatchDescriptionFor(actual: T): String = """"$actual" does not $description"""
}
package io.github.neyb.shoulk.Matcher

interface Matcher<in T> {
    val description:String
    fun match(actual: T): MatchResult
}
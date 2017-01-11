package io.github.neyb.shoulk.matcher

interface Matcher<in T> {
    val description:String
    fun match(actual: T): MatchResult
}
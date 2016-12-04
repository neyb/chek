package io.github.neyb.shoulk.Matcher

infix fun <T> Matcher<T>.and(matcher: Matcher<T>): Matcher<T> = AndMatcher(this, matcher)

class AndMatcher<in T>(vararg private val matchers: Matcher<T>)
    : SimpleMatcher<T>(matchers.joinToString(separator = " and ") { it.description }) {

    override fun doesMatch(actual: T) = matchers.all { it.match(actual).success }

    override fun getDismatchDescriptionFor(actual: T) = matchers.first { !it.match(actual).success }.getDismatchDescriptionFor(actual)
}
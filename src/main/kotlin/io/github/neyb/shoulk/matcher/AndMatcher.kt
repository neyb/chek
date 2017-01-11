package io.github.neyb.shoulk.matcher

infix fun <T> Matcher<T>.and(matcher: Matcher<T>): Matcher<T> = AndMatcher(this, matcher)

class AndMatcher<in T>(vararg private val matchers: Matcher<T>)
    : Matcher<T> {
    override val description = matchers.joinToString(separator = " and ") { it.description }

    override fun match(actual: T) =
            matchers.asSequence()
                    .map { it.match(actual) }
                    .firstOrNull { !it.success}
                    ?:MatchResult.ok
}
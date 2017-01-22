package io.github.neyb.shoulk.matcher

class BasicMatcher<in T>(override val description: String,
                         private val matcher: (T) -> MatchResult) : Matcher<T> {
    override fun match(actual: T): MatchResult = matcher(actual)
}
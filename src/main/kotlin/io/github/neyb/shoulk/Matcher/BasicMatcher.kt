package io.github.neyb.shoulk.Matcher

class BasicMatcher<T>(override val description: String,
                      private val matcher: (T) -> MatchResult) : Matcher<T> {
    override fun match(actual: T): MatchResult = matcher(actual)
}
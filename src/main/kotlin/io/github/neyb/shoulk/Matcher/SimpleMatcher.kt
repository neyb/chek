package io.github.neyb.shoulk.Matcher

abstract class SimpleMatcher<in T>(override val description: String) :Matcher<T>{
    override fun match(actual: T) =
            if(doesMatch(actual)) MatchResult.ok
            else MatchResult.Fail(getDismatchDescriptionFor(actual))
    abstract fun doesMatch(actual: T): Boolean
}
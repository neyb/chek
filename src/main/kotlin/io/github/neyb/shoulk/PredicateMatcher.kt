package io.github.neyb.shoulk

internal class PredicateMatcher<T> (private val matcher:(T)->Boolean) : Matcher<T> {
    override fun match(actual: T) = matcher(actual)
    override fun getDismatchDescriptionFor(actual: T) = "\"$actual\" does not match a not described criteria"
}
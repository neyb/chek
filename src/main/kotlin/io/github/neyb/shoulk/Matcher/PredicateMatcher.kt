package io.github.neyb.shoulk.Matcher

fun <T> match(matcher: (T) -> Boolean): Matcher<T> = PredicateMatcher(matcher)

private class PredicateMatcher<T>(private val matcher: (T) -> Boolean) : Matcher<T> {
    override val description = "..."
    override fun match(actual: T) = matcher(actual)
    override fun getDismatchDescriptionFor(actual: T) = "\"$actual\" does not match a not described criteria"
}
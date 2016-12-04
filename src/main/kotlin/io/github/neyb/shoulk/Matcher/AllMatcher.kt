package io.github.neyb.shoulk.Matcher


internal class AllMatcher<in T>(private val matcher: Matcher<T>)
    : SimpleMatcher<Iterable<T>>("contain only items matching ${matcher.description}") {

    override fun doesMatch(actual: Iterable<T>) = actual.all { matcher.match(it).success}

    override fun getDismatchDescriptionFor(actual: Iterable<T>) = "$actual does not $description" +
            ": items at position ${positions(actual)} does not"

    private fun positions(actual: Iterable<T>) = actual.withIndex().asSequence()
            .filter { !matcher.match(it.value).success }
            .map { it.index }
            .joinToString()
}



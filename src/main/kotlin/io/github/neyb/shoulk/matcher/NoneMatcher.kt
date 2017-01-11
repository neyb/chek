package io.github.neyb.shoulk.matcher


internal class NoneMatcher<in T>(private val matcher: Matcher<T>)
    : SimpleMatcher<Iterable<T>>("contain no item matching ${matcher.description}") {

    override fun doesMatch(actual: Iterable<T>) = actual.none { matcher.match(it).success }

    override fun getDismatchDescriptionFor(actual: Iterable<T>) = "$actual should $description" +
            " but items at position ${positions(actual)} does"

    private fun positions(actual: Iterable<T>) = actual.withIndex().asSequence()
            .filter { matcher.match(it.value).success }
            .map { it.index }
            .joinToString()
}



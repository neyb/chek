package io.github.neyb.shoulk.Matcher


internal class AllMatcher<in T>(private val matcher: Matcher<T>) : Matcher<Iterable<T>> {
    override val description = "contain only items matching ${matcher.description}"

    override fun match(actual: Iterable<T>) = actual.all { matcher.match(it) }

    override fun getDismatchDescriptionFor(actual: Iterable<T>) = "$actual should $description" +
            " but items at position ${positions(actual)} does not"

    private fun positions(actual: Iterable<T>) = actual.withIndex().asSequence()
            .filter { !matcher.match(it.value) }
            .map { it.index }
            .joinToString()
}



package io.github.neyb.shoulk.matcher

internal class AllMatcher<in T>(private val matcher: Matcher<T>)
    : Matcher<Iterable<T>> {
    override val description = "contain only items matching ${matcher.description}"

    override fun match(actual: Iterable<T>): MatchResult {
        val indexedFailResult = actual.withIndex().asSequence()
                .map { it.map { matcher.match(it) } }
                .filter { !it.value.success }
                .map { it.map { it as MatchResult.Fail } }
                .toList()
        return if (indexedFailResult.isEmpty()) MatchResult.ok
        else buildFailMessage(actual, indexedFailResult)
    }

    private fun buildFailMessage(actual: Iterable<T>, indexedFailResult: List<IndexedValue<MatchResult.Fail>>) = MatchResult.Fail(
            indexedFailResult.joinToString(
                    prefix = "\"$actual\" does not matcher all matcher:\n",
                    separator = "\n"
            ) { " * @${it.index}: ${it.value.errorMessage}" })

    private fun <T, Y> IndexedValue<T>.map(transform: (T) -> Y) =
            IndexedValue(this.index, transform(this.value))
}

package io.github.neyb.shoulk.Matcher

import java.util.*

class InOrderMatcher<in T>(val matchers: List<Matcher<T>>) : Matcher<Iterable<T>> {
    override val description = "match matchers"

    override fun match(actual: Iterable<T>): MatchResult {
        val actualElements = ArrayList<T>()
        actualElements.addAll(actual)

        if(actualElements.size != matchers.size)
            return MatchResult.Fail("\"$actual\" has ${getNElementsLabel(actualElements.size)} while it should have ${getNElementsLabel(matchers.size)}")

        val matchErrors = getMatchingErrors(actualElements)

        return if (matchErrors.isEmpty()) MatchResult.ok
        else MatchResult.Fail(buildMatchingFailMessage(actual, matchErrors))
    }

    private fun getMatchingErrors(actualElements: Collection<T>): List<IndexedValue<MatchResult.Fail>> {
        return matchers.zip(actualElements).withIndex().asSequence()
                .map { IndexedValue( it.index, it.value.first.match(it.value.second)) }
                .filter { indexedMatcherToElement -> !indexedMatcherToElement.value.success }
                .map { IndexedValue(it.index, it.value as MatchResult.Fail) }
                .toList()
    }

    private fun getNElementsLabel(n: Int) = "$n ${if (n > 1) "elements" else "element"}"

    private class DifferentSize(val actualSize: Int) : Exception()

    private fun <T> Iterable<T>.collectionSizeOrDefault(default: Int): Int = if (this is Collection<*>) this.size else default

    private fun Iterator<*>.countRemaining(): Int {
        var index = 0
        this.forEach { ++index }
        return index
    }

    private fun buildMatchingFailMessage(actual: Iterable<T>, matchErrors: List<IndexedValue<MatchResult.Fail>>) =
            matchErrors.joinToString(
                    prefix = "\"$actual\" does not match matchers:\n",
                    separator = "\n",
                    transform = { pair -> " * @${pair.index}: ${pair.value.errorMessage}" })

}
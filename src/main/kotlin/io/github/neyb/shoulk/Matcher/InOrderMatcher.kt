package io.github.neyb.shoulk.Matcher

import java.util.*

class InOrderMatcher<in T>(val matchers: List<Matcher<T>>) : Matcher<Iterable<T>> {
    override val description = "match matchers"

    override fun match(actual: Iterable<T>): MatchResult {
        try {
            val matchErrors = ArrayList<IndexedValue<MatchResult.Fail>>()
            for ((index, pair) in zipMatchersTo(actual).withIndex()) {
                val (matcher, value) = pair
                val matchResult = matcher.match(value)
                if (!matchResult.success)
                    matchErrors.add(IndexedValue(index, (matchResult as MatchResult.Fail)))
            }

            return if (matchErrors.isEmpty()) MatchResult.ok
            else MatchResult.Fail(buildMatchingFailMessage(actual, matchErrors))

        } catch (differentSize: DifferentSize) {
            return MatchResult.Fail("\"$actual\" has ${getNElementsLabel(differentSize.actualSize)} while it should have ${getNElementsLabel(matchers.size)}")
        }
    }

    private fun getNElementsLabel(n: Int) = "$n ${if (n > 1) "elements" else "element"}"

    private fun zipMatchersTo(values: Iterable<T>): List<Pair<Matcher<T>, T>> {
        val first = matchers.iterator()
        val second = values.iterator()
        val list = ArrayList<Pair<Matcher<T>, T>>(Math.min(matchers.size, values.collectionSizeOrDefault(10)))
        while (first.hasNext() && second.hasNext())
            list.add(first.next() to second.next())
        val firstSize = list.size + first.countRemaining()
        val secondSize = list.size + second.countRemaining()
        if (firstSize != secondSize) throw DifferentSize(secondSize)
        return list
    }

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
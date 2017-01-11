package io.github.neyb.shoulk.matcher

import java.util.*

class InAnyOrderMatcher<T>(val matchers: List<Matcher<T>>) : Matcher<Iterable<T>> {
    override val description = "match matchers"

    override fun match(actual: Iterable<T>): MatchResult {
        val actualElements = ArrayList<T>()
        actualElements.addAll(actual)

        if (actualElements.size != matchers.size)
            return MatchResult.Fail("\"$actual\" has ${getNElementsLabel(actualElements.size)} while it should have ${getNElementsLabel(matchers.size)}")

//        val indexedError = recMatch(matchers, actualElements)
        return MatchResult.ok
    }

//    private fun recMatch(matchers: List<Matcher<T>>, actualElements: List<T>): IndexedValue<MatchResult.Fail<T>>? {
//        if (actualElements.isEmpty()) return null
//
//        val actualElement = actualElements[0]
//        val matchingMatchers = matchers.withIndex()
//                .filter { it.value.match(actualElement).success }
//        if (matchingMatchers.isEmpty()) return IndexedValue(1, )
//        var maxDepthMatchingMatcher: IndexedValue<Matcher<T>>? = null
//        for ((index, matchingMatcher) in matchingMatchers) {
//            val recMatch = recMatch(matchers.drop(index), actualElements.drop(0)) ?: return null
//            if (maxDepthMatchingMatcher == null || maxDepthMatchingMatcher.index < recMatch.index)
//                maxDepthMatchingMatcher = recMatch
//        }
//        listOf<>()
//        return IndexedValue(maxDepthMatchingMatcher.index + 1, )
//
//    }

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
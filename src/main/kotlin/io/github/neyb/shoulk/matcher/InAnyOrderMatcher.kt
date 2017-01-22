package io.github.neyb.shoulk.matcher

import io.github.neyb.shoulk.util.removeDuplicateIndexes
import io.github.neyb.shoulk.util.reverse
import java.util.*

class InAnyOrderMatcher<in T>(val matchers: List<Matcher<T>>) : Matcher<Iterable<T>> {
    override val description = "match matchers"

    override fun match(actual: Iterable<T>): MatchResult {
        val actualElements = ArrayList<T>()
        actualElements.addAll(actual)

        if (actualElements.size != matchers.size)
            return MatchResult.Fail("\"$actual\" has ${getNElementsLabel(actualElements.size)} while it should have ${getNElementsLabel(matchers.size)}")

        val matchingMatcherIndexes = actualElements.map { actualElement ->
            matchers.asSequence()
                    .withIndex()
                    .filter { it.value.match(actualElement).success }
                    .map { it.index }
                    .toSet()
        }

        val solutionMatchingMatcherIndexes = matchingMatcherIndexes.solve()
        val indexWithoutMatchers = solutionMatchingMatcherIndexes.asSequence()
                .withIndex()
                .filter { it.value == null }
                .map { it.index }
                .toSet()
        return if (indexWithoutMatchers.isEmpty()) MatchResult.ok
        else MatchResult.Fail("???")
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

    private fun List<Set<Int>>.solve(): List<Int?> {
        var currentIndexes: List<MutableSet<Int>> = this.map { HashSet(it) }
        do {
            currentIndexes = currentIndexes.safeSolve()
        } while (currentIndexes.removeFirstDuplicateValue())
        return currentIndexes.map { it.firstOrNull() }
    }

    private fun List<MutableSet<Int>>.safeSolve(): List<MutableSet<Int>> {
        return this.removeDuplicateIndexes()
                .reverse()
                .removeDuplicateIndexes()
                .reverse()
                .map { it.toMutableSet() }
    }

    private fun List<MutableSet<Int>>.removeFirstDuplicateValue(): Boolean {
        for (current in this)
            if (current.size > 1) {
                with(current.iterator()) {
                    next()
                    remove()
                }
                return true
            }
        return false
    }

}

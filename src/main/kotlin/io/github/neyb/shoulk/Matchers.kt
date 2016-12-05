package io.github.neyb.shoulk

import io.github.neyb.shoulk.Matcher.*

fun <T> equal(expected: T) = match<T>("""be equal to "$expected"""") { it == expected }

fun <T> be(expected: T) = match<T>("""be the same object as "$expected"@${System.identityHashCode(expected)}""")
{ it === expected } but { "its identity hashCode is @${System.identityHashCode(it)}" }

fun <T> contain(expected: T) = match<Iterable<T>>("""contain "$expected"""") { it.contains(expected) }

fun <T> contain(matcher: Matcher<T>) = match<Iterable<T>>("""contain an element matching "${matcher.description}"""")
{ it.any { matcher.match(it).success } }

fun <T> matchInOrder(matchers: List<Matcher<T>>) = InOrderMatcher(matchers)

private fun <T> Iterable<T>.getFirstDismatchIndex(matchers: List<Matcher<T>>): Int? {
    this.forEachIndexed { index, curr ->
        if (!matchers[index].match(curr).success) return index
    }
    return null
}

fun <T> all(matcher: Matcher<T>): Matcher<Iterable<T>> = AllMatcher(matcher)

fun <T> noneMatch(matcher: Matcher<T>): Matcher<Iterable<T>> = NoneMatcher(matcher)

private fun haveSize(size: Int) = match<Iterable<*>>("have $size element(s)") { it.size() == size } but { "has ${it.size()} element(s)" }

private fun Iterable<*>.size(): Int {
    var size = 0
    this.forEach { ++size }
    return size
}

private operator fun <T> Iterable<T>.get(index: Int): T {
    if (index >= 0) this.forEachIndexed { currIndex, curr -> if (currIndex == index) return curr }
    throw IndexOutOfBoundsException("$this has no item at $index")
}
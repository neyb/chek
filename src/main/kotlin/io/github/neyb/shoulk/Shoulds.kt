package io.github.neyb.shoulk

import kotlin.reflect.KClass
import kotlin.test.assertFailsWith

infix fun <T> T.shouldEquals(expected: T) = shouldMatch(equalsTo(expected))

@Suppress("UNUSED_PARAMETER") // for infix code
infix inline fun <reified E : Throwable> (() -> Any).shouldThrow(expectedType: KClass<E>) =
        assertFailsWith<E> { this() }
infix fun <E : Throwable> E.that(matcher: Matcher<E>) = shouldMatch(matcher)
infix fun <E : Throwable> E.that(matcher: (E) -> Boolean) = shouldMatch(matcher)

infix inline fun <reified T : Throwable> (() -> Any).shouldThrow(noinline matcher: (T) -> Boolean) =
        assertFailsWith<T> { this() } shouldMatch matcher

infix inline fun <reified T : Throwable> (() -> Any).shouldThrow(matcher: Matcher<T>) =
        assertFailsWith<T> { this() } shouldMatch matcher

infix fun <T> T.shouldMatch(matcher: (T) -> Boolean) = shouldMatch(PredicateMatcher(matcher))

infix fun <T> T.shouldMatch(matcher: Matcher<T>) {
    if (!matcher.match(this)) throw AssertionError(matcher.getDismatchDescriptionFor(this))
}


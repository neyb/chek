package io.github.neyb.shoulk

import kotlin.reflect.KClass
import kotlin.test.assertFailsWith

infix fun <T> T.shouldMatch(matcher: Matcher<T>) {
    if (!matcher.match(this)) throw AssertionError(matcher.assertionErrorMessage(this))
}

infix fun <T> T.shouldMatch(matcher: (T) -> Boolean) = this shouldMatch FluentMatcher(matcher)

infix fun <T> T.shouldEquals(expected: T) = shouldMatch(eq(expected))

infix inline fun <reified T : Throwable> (() -> Any).shouldThrow(matcher: Matcher<T>) =
        assertFailsWith<T> { this() } shouldMatch matcher

infix inline fun <reified T : Throwable> (() -> Any).shouldThrow(noinline matcher: (T) -> Boolean) =
        assertFailsWith<T> { this() } shouldMatch matcher

@Suppress("UNUSED_PARAMETER") // for infix code
infix inline fun <reified E : Throwable> (() -> Any).shouldThrow(expectedType: KClass<E>) =
        assertFailsWith<E> { this() }

infix fun <E : Throwable> E.that(matcher: Matcher<E>) = this shouldMatch matcher
infix fun <E : Throwable> E.that(matcher: (E) -> Boolean) = this shouldMatch matcher

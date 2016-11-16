package io.github.neyb.shoulk

import kotlin.test.assertFails

infix fun <T> T.shouldMatch(matcher: Matcher<T>) {
    if (!matcher.match(this)) throw AssertionError(matcher.assertionErrorMessage(this))
}

infix fun <T> T.shouldMatch(matcher: (T) -> Boolean) = this shouldMatch FluentMatcher(matcher)

infix fun <T> T.shouldEquals(expected: T) = shouldMatch (eq(expected))

infix inline fun <reified T:Exception>(() -> Any).shouldThrow(matcher: Matcher<T>) =
        assertFails { this() } shouldMatch matcher

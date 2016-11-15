package io.github.neyb.tchekik

import io.github.neyb.tchekik.matcher.FluentMatcher
import io.github.neyb.tchekik.matcher.Matcher
import kotlin.test.assertEquals
import kotlin.test.fail


infix fun Any.shouldEquals(expected: Any) = assertEquals(this, expected)

infix fun <T> T.shouldMatch(matcher: (T) -> Boolean) {
    this.shouldMatch(FluentMatcher(matcher))
}

infix fun <T> T.shouldMatch(matcher: Matcher<T>) {
    if (!matcher.match(this)) throw AssertionError(matcher.assertionErrorMessage(this))
}
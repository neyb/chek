package io.github.neyb.shoulk

import io.github.neyb.shoulk.matcher.FluentMatcher
import io.github.neyb.shoulk.matcher.Matcher
import kotlin.test.assertEquals
import kotlin.test.fail


infix fun Any.shouldEquals(expected: Any) = assertEquals(this, expected)

infix fun <T> T.shouldMatch(matcher: (T) -> Boolean) {
    this.shouldMatch(FluentMatcher(matcher))
}

infix fun <T> T.shouldMatch(matcher: Matcher<T>) {
    if (!matcher.match(this)) throw AssertionError(matcher.assertionErrorMessage(this))
}
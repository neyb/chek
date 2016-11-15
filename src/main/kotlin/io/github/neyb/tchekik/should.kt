package io.github.neyb.tchekik

import io.github.neyb.tchekik.matcher.Matcher
import kotlin.test.assertEquals
import kotlin.test.fail


infix fun Any.shouldEquals(expected: Any) = assertEquals(this, expected)

infix fun <T> T.shouldMatch(matcher: Matcher<T>): T {
    if (!matcher.match(this))
        throw AssertionError(matcher.assertionErrorMessage(this))
    return this
}

//infix fun <T> T.shouldMatch(matcher: (T) -> Boolean): T {
//    return shouldMatch({ "$it does not match expectation" }, matcher)
//}
//
//fun <T> T.shouldMatch(messageBuilder: ((resulting: T) -> String)? = null, matcher: (T) -> Boolean): T {
//    if (!matcher(this)) fail("")
//    return this
//}


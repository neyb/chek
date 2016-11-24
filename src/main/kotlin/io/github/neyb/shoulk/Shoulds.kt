package io.github.neyb.shoulk

import io.github.neyb.shoulk.Matcher.*
import kotlin.reflect.KClass
import kotlin.test.assertFailsWith

infix fun <T> T.shouldEqual(expected: T) = shouldMatch(equalTo(expected))
infix fun <T> T.shouldNotEqual(expected: T) = shouldMatch(!equalTo(expected))

infix fun <T> T.shouldBe(expected: T) = shouldMatch(sameAs(expected))
infix fun <T> T.shouldNotBe(expected: T) = shouldMatch(!sameAs(expected))

infix fun <T> Iterable<T>.shouldContain(expected: T) = shouldMatch(contain(expected))
infix fun <T> Iterable<T>.shouldNotContain(expected: T) = shouldMatch(!contain(expected))

infix fun <T> Iterable<T>.anyShouldMatch(matcher: Matcher<T>) = shouldMatch(anyMatch(matcher))
infix fun <T> Iterable<T>.noneShouldMatch(matcher: Matcher<T>) = shouldMatch(anyMatch(matcher))
infix fun <T> Iterable<T>.allShouldMatch(matcher: Matcher<T>) = shouldMatch(!anyMatch(matcher))

@Suppress("UNUSED_PARAMETER") // for infix code
infix inline fun <reified E : Throwable> (() -> Any).shouldThrow(expectedType: KClass<E>) =
        assertFailsWith<E> { this() }
infix fun <E : Throwable> E.that(predicate: (E) -> Boolean) = shouldMatch(predicate)
infix fun <E : Throwable> E.that(matcher: Matcher<E>) = shouldMatch(matcher)

infix inline fun <reified T : Throwable> (() -> Any).shouldThrow(noinline predicate: (T) -> Boolean) =
        assertFailsWith<T> { this() } shouldMatch predicate

infix inline fun <reified T : Throwable> (() -> Any).shouldThrow(matcher: Matcher<T>) =
        assertFailsWith<T> { this() } shouldMatch matcher

infix fun <T> T.shouldMatch(matcher: (T) -> Boolean) = shouldMatch(PredicateMatcher(matcher))

infix fun <T> T.shouldMatch(matcher: Matcher<T>) {
    if (!matcher.match(this)) throw AssertionError(matcher.getDismatchDescriptionFor(this))
}


package io.github.neyb.shoulk

import io.github.neyb.shoulk.Matcher.*
import kotlin.reflect.KClass
import kotlin.test.assertFailsWith

infix fun <T> T.shouldEqual(expected: T) = should(equal(expected))
infix fun <T> T.shouldNotEqual(expected: T) = should(!equal(expected))

infix fun <T> T.shouldBe(expected: T) = should(be(expected))
infix fun <T> T.shouldNotBe(expected: T) = should(!be(expected))

infix fun <T> Iterable<T>.shouldContain(expected: T) = should(contain(expected))
infix fun <T> Iterable<T>.shouldContain(matcher: Matcher<T>) = should(contain(matcher))

infix fun <T> Iterable<T>.shouldNotContain(expected: T) = should(!contain(expected))

infix fun <T> Iterable<T>.shouldMatchInOrder(matchers: List<Matcher<T>>) = should(matchInOrder(matchers))


//infix fun <T> Iterable<T>.anyShouldMatch(matcher: Matcher<T>) = should(anyMatch(matcher))
//infix fun <T> Iterable<T>.noneShouldMatch(matcher: Matcher<T>) = should(anyMatch(matcher))
//infix fun <T> Iterable<T>.allShouldMatch(matcher: Matcher<T>) = should(!anyMatch(matcher))

@Suppress("UNUSED_PARAMETER") // for infix code
infix inline fun <reified E : Throwable> (() -> Any).shouldThrow(expectedType: KClass<E>) =
        assertFailsWith<E> { this() }
infix fun <E : Throwable> E.that(matcher: Matcher<E>) = should(matcher)

infix fun <T> T.should(matcher: Matcher<T>) {
    if (!matcher.match(this)) throw AssertionError(matcher.getDismatchDescriptionFor(this))
}


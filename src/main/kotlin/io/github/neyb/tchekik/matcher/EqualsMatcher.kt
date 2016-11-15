package io.github.neyb.tchekik.matcher

import kotlin.test.assertEquals

fun <T> equalsTo(expected:T) = EqualsMatcher(expected)
class EqualsMatcher<T>(private val expected: T) : Matcher<T> {

    override fun match(actual: T)=  actual == expected
    override fun assertionErrorMessage(actual: T) =
            "the result $actual is expected to be equals to $expected"
}



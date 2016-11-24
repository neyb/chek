package io.github.neyb.shoulk.Matcher

fun <T> equalTo(expected: T) = { it: T -> it == expected } describedAs
        """be equals to "$expected""""

fun <T> sameAs(expected: T) = { it: T -> it === expected } describedAs
        """be the same object as "$expected"@${System.identityHashCode(expected)}""" but
        { "its identity hashCode is @${System.identityHashCode(it)}" }

fun <T> contain(expected: T) = { it: Iterable<T> -> it.contains(expected) } describedAs
        """contain "$expected""""

fun <T> anyMatch(matcher: Matcher<T>) = { it: Iterable<T> -> it.any { matcher.match(it) } } describedAs
        """contain at least one element matching "${matcher.description} """"

fun <T> allMatch(matcher: Matcher<T>): Matcher<Iterable<T>> = AllMatcher(matcher)

fun <T> noneMatch(matcher: Matcher<T>): Matcher<Iterable<T>> = NoneMatcher(matcher)

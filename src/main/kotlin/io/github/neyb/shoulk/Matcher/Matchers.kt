package io.github.neyb.shoulk.Matcher

fun <T> equalsTo(expected: T) = { it: T -> it == expected } describedAs
        """be equals to "$expected""""

fun <T> sameAs(expected: T) = { it: T -> it === expected } describedAs
        """be the same object as "$expected"@${System.identityHashCode(expected)}""" but
        { "its identity hashCode is @${System.identityHashCode(it)}" }

fun <T> contains(expected: T) = { it: Iterable<T> -> it.contains(expected) } describedAs
        """contain "$expected""""
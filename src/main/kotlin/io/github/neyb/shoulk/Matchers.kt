package io.github.neyb.shoulk

fun <T> equalsTo(expected: T) = { it: T -> it == expected } describedAs
        """be equals to "$expected""""

fun <T> sameAs(expected: T) = { it: T -> it === expected } describedAs
        """be the same object as "$expected"@${System.identityHashCode(expected)}""" but
        { it -> "its identity hashCode is @${System.identityHashCode(it)}" }


package io.github.neyb.shoulk

fun <T> eq(expected: T) = { it: T -> it == expected } describedAs
        "be equals to $expected"


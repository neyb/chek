package io.github.neyb.shoulk

fun <T> equalsTo(expected: T) = { it: T -> it == expected } describedAs """be equals to "$expected""""


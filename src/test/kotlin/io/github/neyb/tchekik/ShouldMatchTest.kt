package io.github.neyb.tchekik

import io.github.neyb.tchekik.matcher.described
import io.github.neyb.tchekik.matcher.equalsTo
import org.jetbrains.spek.api.Spek

class ShouldMatchTest : Spek({
    test("test") {
        "aaa" shouldMatch (equalsTo("aaa") and equalsTo("aaa"))
    }

    test("test2") {
        "aaa" shouldMatch described({"$it should have a size of 3"}) {it.length == 3} and
    }

    test("test3") {
        "aa" shouldMatch described({"$it should have a size of 3 but has a size of ${it.length}"}) {it.length == 3}
    }
})
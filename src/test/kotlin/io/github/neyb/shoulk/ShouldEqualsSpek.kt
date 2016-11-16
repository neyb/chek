package io.github.neyb.shoulk

import org.jetbrains.spek.api.Spek
import kotlin.test.assertFailsWith

class ShouldEqualsSpek : Spek({
    test("aaa should equals aaa") {
        "aaa" shouldEquals "aaa"
    }

    test("aaa shouldEqual bbb throw exception") {
        assertFailsWith<AssertionError> {
            "aaa" shouldEquals "bbb"
        }
    }
})
package io.github.neyb.tchekik

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import kotlin.test.*

class ShouldEqualsTest : Spek({
    test("aaa should equals aaa") {
        "aaa" shouldEquals "aaa"
    }

    test("aaa shouldEqual bbb throw exception") {
        assertFailsWith<AssertionError> {
            "aaa" shouldEquals "bbb"
        }
    }
})
package io.github.neyb.shoulk

import io.github.neyb.shoulk.matcher.describedAs
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import kotlin.test.*

class ShouldMatchTest : Spek({
    test("comparing to same should pass") {
        "aaa" shouldMatch { it == "aaa" }
    }

    test("testing right length should pass") {
        "aaa" shouldMatch { it.length == 3 }
    }

    given("a failing test without message") {
        val failingTest = { "aa" shouldMatch { it.length == 3 } }

        it("should throw assertionError") {
            assertFailsWith<AssertionError> { failingTest() }
        }

        it("should throw the right message") {
            val e = assertFails { failingTest() }
            assertEquals(""""aa" should match a not described criteria""", e.message)
        }
    }

    given("a failing test with a message") {
        val failingTest = {
            "aa" shouldMatch ({ it: String -> it.length == 3 }
                    describedAs "should have a size of 3")
        }

        it("should throw the right message") {
            val e = assertFails { failingTest() }
            assertEquals(""""aa" should have a size of 3""", e.message)
        }
    }

    given("a failing test with a message & failure message") {
        val failingTest = {
            "aa" shouldMatch ({ it: String -> it.length == 3 }
                    describedAs "should have a size of 3" but { "has a size of ${it.length}" })
        }

        it("should throw the right message") {
            val e = assertFails { failingTest() }
            assertEquals(""""aa" should have a size of 3 but has a size of 2""", e.message)
        }
    }


})
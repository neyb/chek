package io.github.neyb.shoulk

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import java.util.*
import kotlin.test.*

class ShouldsSpek : Spek({

    group("basics expectation") {
        group("shouldEquals") {
            test("aaa should equals aaa") {
                "aaa" shouldEquals "aaa"
            }

            test("aaa shouldEqual bbb throw exception") {
                val failMessage = assertFailsWith<AssertionError> {
                    "aaa" shouldEquals "bbb"
                }.message
                assertEquals(""""aaa" should be equals to "bbb"""", failMessage)
            }

        }

    }

    group("shouldThrow") {
        // throw a ArrayIndexOutOfBoundsException which extends IndexOutOfBoundsException
        // but it is not a IllegalStateException
        val failing = { ArrayList<String>()[0] }

        it("should pass `shouldThrow { okMatching } `") {
            failing shouldThrow { e: IndexOutOfBoundsException -> e.message != null }
        }

        it("should pass `.shouldThrow<IndexOutOfBoundsException>{it.message != null}`") {
            failing.shouldThrow<IndexOutOfBoundsException> { it.message != null }
        }

        it("should pass `.shouldThrow<IndexOutOfBoundsException>{it.message != null}`") {
            failing.shouldThrow({ e: IndexOutOfBoundsException -> e.message != null } describedAs "have no message")
        }

        it("should pass `failing shouldThrow IndexOutOfBoundsException::class that { it.message != null }`") {
            failing shouldThrow IndexOutOfBoundsException::class that { it.message != null }
        }
        group("with predicate") {
            it("should pass if predicate returns true") {
                failing shouldThrow { e: Throwable -> e is IndexOutOfBoundsException }
            }


        }
        group("with matcher") { }
        group("with expected Throwable class") { }

    }

    group("generic matching") {
        group("shouldMatch") {
            group("with predicate") {
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
                        val failingMessage = assertFails { failingTest() }.message
                        assertEquals(""""aa" does not match a not described criteria""", failingMessage)
                    }
                }

            }

            group("with predifined matchers") {
                "aaa" shouldMatch equalsTo("aaa")
            }

            group("with fluent matcher") {
                it("is created with describedAs function") {
                    "aaa" shouldMatch ({ it: String -> it.length == 3 } describedAs "have a size of 3")
                }

                it("build the fail message for you") {
                    val failMessage = assertFails {
                        "aa" shouldMatch ({ it: String -> it.length == 3 } describedAs "have a size of 3")
                    }.message
                    assertEquals("\"aa\" should have a size of 3", failMessage)
                }

                test("you can specify the error cause with `but` function") {
                    val failMessage = assertFails {
                        "aa" shouldMatch ({ it: String -> it.length == 3 }
                                describedAs "have a size of 3" but { "has a size of ${it.length}" })
                    }.message
                    assertEquals("\"aa\" should have a size of 3 but has a size of 2", failMessage)
                }
            }

            group("with your own matcher") {
                class SizeOf(private val expectedSize: Int) : Matcher<String> {
                    override fun match(actual: String) = actual.length == expectedSize
                    override fun getDismatchDescriptionFor(actual: String) = "$actual has not a size of $expectedSize"
                }

                it("it can pass") {
                    "aaa" shouldMatch SizeOf(3)
                }

                it("it can fail with your message") {
                    val failMessage = assertFails { "aaa" shouldMatch SizeOf(2) }.message
                    assertEquals("aaa has not a size of 2", failMessage)
                }

            }
        }
    }

})
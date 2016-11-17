package io.github.neyb.shoulk

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import java.util.*
import kotlin.test.*

class ShouldsSpek : Spek({

    group("basics expectation") {
        group("testing equality") {
            group("shouldEqual") {
                test("aaa should equals aaa") {
                    "aaa" shouldEqual "aaa"
                }

                it("fails with right exception") {
                    val failMessage = assertFailsWith<AssertionError> {
                        "aaa" shouldEqual "bbb"
                    }.message
                    assertEquals(""""aaa" should be equals to "bbb"""", failMessage)
                }
            }

            group("shouldNotEquals") {
                test("aaa should equals aaa") {
                    "aaa" shouldNotEqual "bbb"
                }

                it("fails with right exception") {
                    val failMessage = assertFailsWith<AssertionError> {
                        "aaa" shouldNotEqual "aaa"
                    }.message
                    assertEquals(""""aaa" should not be equals to "aaa"""", failMessage)
                }
            }
        }

        group("testing identity") {
            val o1 = "dog"
            val o2 = o1
            val o3 = "cat"

            group("shouldBe") {

                test("o2 should be o1") {
                    o2 shouldBe o1
                }

                it("fails with right message") {
                    val failMessage = assertFails {
                        o1 shouldBe o3
                    }.message

                    assertTrue(failMessage
                            ?.matches(Regex(""""dog" should be the same object as "cat"@\d+ but its identity hashCode is @\d+"""))
                            ?: false)
                }
            }

            group("shouldNotBe") {
                test("o1 should not be o3") {
                    o1 shouldNotBe o3
                }

                it("fails with right message") {
                    val failMessage = assertFails {
                        o1 shouldNotBe o2
                    }.message

                    assertFalse(failMessage
                            ?.matches(Regex(""""dog" should be the same object as "cat"@\d+ but its identity hashCode is @\d+"""))
                            ?: true)
                }
            }
        }

        group("testing collection") {
            group("shouldContain") { }
            group("shouldNotContain") { }
        }

        group("testing exception") {
            group("shouldThrow") {
                // throw a ArrayIndexOutOfBoundsException which extends IndexOutOfBoundsException
                // but it is not a IllegalStateException
                val failing = { ArrayList<String>()[0] }

                group("allow to test exception type") {
                    test("passing test") {
                        failing shouldThrow IndexOutOfBoundsException::class
                    }

                    test("failing test got right message") { }
                }
                group("with message") { }
                group("with predicate") {
                    it("passing test") {
                        failing shouldThrow IndexOutOfBoundsException::class that { it.message != null }
                    }

                    test("other passing test") { // same as above, another syntax
                        failing shouldThrow { e: IndexOutOfBoundsException -> e.message != null }
                    }

                    it("and another passing test") { // still same effect, another syntax
                        failing.shouldThrow<IndexOutOfBoundsException> { it.message != null }
                    }
                }
                group("with matcher") {
                    it("should pass `.shouldThrow<IndexOutOfBoundsException>{it.message != null}`") {
                        failing shouldThrow ({ e: IndexOutOfBoundsException -> e.message != null }
                                describedAs "have no message")
                    }
                }
            }
        }
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
                    assertEquals(""""aa" should have a size of 3""", failMessage)
                }

                test("you can specify the error cause with `but` function") {
                    val failMessage = assertFails {
                        "aa" shouldMatch ({ it: String -> it.length == 3 }
                                describedAs "have a size of 3" but { "has a size of ${it.length}" })
                    }.message
                    assertEquals(""""aa" should have a size of 3 but has a size of 2""", failMessage)
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
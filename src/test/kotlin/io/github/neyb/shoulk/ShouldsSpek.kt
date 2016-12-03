package io.github.neyb.shoulk

import io.github.neyb.shoulk.Matcher.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import java.util.*
import kotlin.test.*

class ShouldsSpek : Spek({

    group("basics expectation") {
        group("testing equality") {
            group("shouldEqual") {
                test("dog should equals dog") {
                    "dog" shouldEqual "dog"
                }

                it("fails with right exception") {
                    val failMessage = assertFailsWith<AssertionError> {
                        "dog" shouldEqual "cat"
                    }.message
                    assertEquals(""""dog" should be equals to "cat"""", failMessage)
                }
            }

            group("shouldNotEquals") {
                test("dog should Not equals cat") {
                    "dog" shouldNotEqual "cat"
                }

                it("fails with right exception") {
                    val failMessage = assertFailsWith<AssertionError> {
                        "dog" shouldNotEqual "dog"
                    }.message
                    assertEquals(""""dog" should not be equals to "dog"""", failMessage)
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

                    failMessage should match { it?.matches(Regex(""""dog" should be the same object as "cat"@\d+ but its identity hashCode is @\d+""")) ?: false }

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

        group("testing collection (actually its based on iterable)") {
            val list = listOf("cat", "dog")

            group("shouldContain") {
                test("list should contain cat") {
                    list shouldContain "cat"
                }

                it("fails with right exception") {
                    val failMessage = assertFails {
                        list shouldContain "kitty"
                    }.message
                    assertEquals(""""[cat, dog]" should contain "kitty"""", failMessage)
                }
            }
            group("shouldNotContain") {
                test("list should not contain kitty") {
                    list shouldNotContain "kitty"
                }

                it("fails with right exception") {
                    val failMessage = assertFails {
                        list shouldNotContain "cat"
                    }.message
                    assertEquals(""""[cat, dog]" should not contain "cat"""", failMessage)
                }
            }
            group("shouldContainAny") { }
            group("shouldContainAll") { }

            group("generic matching") {
                group("any match") {
                    test("list shouldContain beginning with c") {
                        list shouldContain match { it[0] == 'c' }
                    }

                    it("fails with right exception") {
                        val failMessage = assertFails {
                            list shouldContain match("start with a z") { it[0] == 'z' }
                        }.message
                        assertEquals(""""[cat, dog]" should contain an element matching "start with a z"""", failMessage)
                    }

                    it("fails with right exception") {
                        val failMessage = assertFails {
                            list shouldContain match("start with a z") { it[0] == 'z' }
                        }.message
                        assertEquals(""""[cat, dog]" should contain an element matching "start with a z"""", failMessage)
                    }
                }
                group("match in order") {
                    test("matchInOrder passing test") {
                        list shouldMatchInOrder listOf(
                                match("start with a 'c'") { s: String -> s[0] == 'c' },
                                match("start with a 'd'") { s: String -> s[0] == 'd' })
                    }

                    test("matchInOrder should check size first and fail with right message") {
                        val failMessage = assertFails {
                            list shouldMatchInOrder listOf(
                                    match("start with a 'a'") { s: String -> s[0] == 'a' },
                                    match("start with a 'c'") { s: String -> s[0] == 'c' },
                                    match("start with a 'd'") { s: String -> s[0] == 'd' }
                            )
                        }.message
                        assertEquals(
                                """"[cat, dog]" should have 3 element(s) but has 2 element(s)""",
                                failMessage
                        )
                    }

                    test("matchInOrder fail with right message") {
                        val failMessage = assertFails {
                            list shouldMatchInOrder listOf(
                                    match("start with a 'a'") { s: String -> s[0] == 'a' },
                                    match("start with a 'd'") { s: String -> s[0] == 'd' })
                        }.message
                        assertEquals(
                                """"[cat, dog]" should match matchers but it fails because "cat" should start with a 'a'""",
                                failMessage
                        )
                    }
                }
                group("match in any order") { }
                group("all match") { }
                group("none match") { }
            }
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

                group("with matcher") {
                    it("should pass `.shouldThrow<IndexOutOfBoundsException>{it.message != null}`") {
                        failing shouldThrow IndexOutOfBoundsException::class that
                                match("have no message") { it.message != null }
                    }
                }
            }
        }
    }

    group("generic matching") {
        group("should") {
            group("with predicate") {
                test("comparing to same should pass") {
                    "dog" should match { it == "dog" }
                }

                test("testing right length should pass") {
                    "dog" should match { it.length == 3 }
                }

                given("a failing test without message") {
                    val failingTest = { "kitty" should match { it.length == 3 } }

                    it("should throw assertionError") {
                        assertFailsWith<AssertionError> { failingTest() }
                    }

                    it("should throw the right message") {
                        val failingMessage = assertFails { failingTest() }.message
                        assertEquals(""""kitty" should match an undescribed criteria""", failingMessage)
                    }
                }

            }

            group("with predifined matchers") {
                "dog" should equal("dog")
            }

            group("with fluent matcher") {
                it("is created with describedAs function") {
                    "dog" should match("have a size of 3") { it.length == 3 }
                }

                it("build the fail message for you") {
                    val failMessage = assertFails {
                        "kitty" should match("have a size of 3") { it.length == 3 }
                    }.message
                    assertEquals(""""kitty" should have a size of 3""", failMessage)
                }

                test("you can specify the error cause with `but` function") {
                    val failMessage = assertFails {
                        "kitty" should (match<String>("have a size of 3") { it.length == 3 }
                                but { "has a size of ${it.length}" })
                    }.message
                    assertEquals(""""kitty" should have a size of 3 but has a size of 5""", failMessage)
                }
            }

            group("with your own matcher") {
                class SizeOfMatcher(private val expectedSize: Int) : Matcher<String> {
                    override val description = "has a size of $expectedSize"
                    override fun match(actual: String) = actual.length == expectedSize
                    override fun getDismatchDescriptionFor(actual: String) = "$actual has not a size of $expectedSize"
                }

                it("it can pass") {
                    "dog" should SizeOfMatcher(3)
                }

                it("it can fail with your message") {
                    val failMessage = assertFails { "dog" should SizeOfMatcher(2) }.message
                    assertEquals("dog has not a size of 2", failMessage)
                }

            }
        }

        group("generic list treatment") {

        }
    }

})
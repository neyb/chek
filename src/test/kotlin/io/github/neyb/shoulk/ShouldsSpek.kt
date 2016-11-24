package io.github.neyb.shoulk

import com.sun.org.glassfish.gmbal.Description
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
            group("any match") { }
            group("match in order") { }
            group("all match") { }
            group("none match") { }
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
                    "dog" shouldMatch { it == "dog" }
                }

                test("testing right length should pass") {
                    "dog" shouldMatch { it.length == 3 }
                }

                given("a failing test without message") {
                    val failingTest = { "kitty" shouldMatch { it.length == 3 } }

                    it("should throw assertionError") {
                        assertFailsWith<AssertionError> { failingTest() }
                    }

                    it("should throw the right message") {
                        val failingMessage = assertFails { failingTest() }.message
                        assertEquals(""""kitty" does not match a not described criteria""", failingMessage)
                    }
                }

            }

            group("with predifined matchers") {
                "dog" shouldMatch equalTo("dog")
            }

            group("with fluent matcher") {
                it("is created with describedAs function") {
                    "dog" shouldMatch ({ it: String -> it.length == 3 } describedAs "have a size of 3")
                }

                it("build the fail message for you") {
                    val failMessage = assertFails {
                        "kitty" shouldMatch ({ it: String -> it.length == 3 } describedAs "have a size of 3")
                    }.message
                    assertEquals(""""kitty" should have a size of 3""", failMessage)
                }

                test("you can specify the error cause with `but` function") {
                    val failMessage = assertFails {
                        "kitty" shouldMatch ({ it: String -> it.length == 3 }
                                describedAs "have a size of 3" but { "has a size of ${it.length}" })
                    }.message
                    assertEquals(""""kitty" should have a size of 3 but has a size of 5""", failMessage)
                }
            }

            group("with your own matcher") {
                class SizeOf(private val expectedSize: Int) : Matcher<String> {
                    override val description = "has a size of $expectedSize"
                    override fun match(actual: String) = actual.length == expectedSize
                    override fun getDismatchDescriptionFor(actual: String) = "$actual has not a size of $expectedSize"
                }

                it("it can pass") {
                    "dog" shouldMatch SizeOf(3)
                }

                it("it can fail with your message") {
                    val failMessage = assertFails { "dog" shouldMatch SizeOf(2) }.message
                    assertEquals("dog has not a size of 2", failMessage)
                }

            }
        }
    }

})
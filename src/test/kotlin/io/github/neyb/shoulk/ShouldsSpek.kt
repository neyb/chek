package io.github.neyb.shoulk

import io.github.neyb.shoulk.matcher.*
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
                    { "dog" shouldEqual "cat" } shouldThrow AssertionError::class that
                            hasMessage(""""dog" is not equal to "cat"""")
                }
            }

            group("shouldNotEquals") {
                test("dog should Not equals cat") {
                    "dog" shouldNotEqual "cat"
                }

                it("fails with right exception") {
                    { "dog" shouldNotEqual "dog" } shouldFailWithMessage """"dog" is equal to "dog""""
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
                    { o1 shouldBe o3 } shouldFailWithAMessageThat match {
                        it.matches(Regex(""""dog" is not the same object as "cat"@\d+: its identity hashCode is @\d+"""))
                    }
                }
            }

            group("shouldNotBe") {
                test("o1 should not be o3") {
                    o1 shouldNotBe o3
                }

                it("fails with right message") {
                    { o1 shouldNotBe o2 } shouldFailWithAMessageThat match {
                        it.matches(Regex(""""dog" is the same object as "dog"@\d+: its identity hashCode is @\d+"""))
                    }
                }
            }
        }

        group("testing collection (actually its based on iterable)") {
            val list = listOf("cat", "dog")
            fun startWith(letter:Char) = match<String>("start with a '$letter'") { it[0] == letter }

            group("shouldContain") {
                test("list should contain cat") {
                    list shouldContain "cat"
                }

                it("fails with right exception") {
                    { list shouldContain "kitty" } shouldFailWithMessage """"[cat, dog]" does not contain "kitty""""
                }
            }
            group("shouldNotContain") {
                test("list should not contain kitty") {
                    list shouldNotContain "kitty"
                }

                it("fails with right exception") {
                    { list shouldNotContain "cat" } shouldFailWithMessage """"[cat, dog]" does contain "cat""""
                }
            }
            group("shouldContainAny") { }
            group("shouldContainAll") { }

            group("generic matching") {
                group("contain") {
                    test("list shouldContain beginning with c") {
                        list shouldContain startWith('c')
                    }

                    it("fails with right exception") {
                        { list shouldContain startWith('z') } shouldFailWithMessage
                                """"[cat, dog]" does not contain an element matching "start with a 'z'""""
                    }
                }
                group("match in order") {
                    test("matchInOrder passing test") {
                        list shouldMatchInOrder listOf(
                                startWith('c'),
                                startWith('d'))
                    }

                    test("matchInOrder fails with right message") {
                        {
                            list shouldMatchInOrder listOf(
                                    startWith('a'),
                                    startWith('q'))
                        } shouldFailWithMessage
                                """"[cat, dog]" does not match matchers:
                                   | * @0: "cat" does not start with a 'a'
                                   | * @1: "dog" does not start with a 'q'""".trimMargin()
                    }

                    test("matchInOrder checks size first and fails with right message (1)") {
                        {
                            list shouldMatchInOrder listOf(
                                    startWith('A'),
                                    startWith('a'),
                                    startWith('a'))
                        } shouldFailWithMessage
                                """"[cat, dog]" has 2 elements while it should have 3 elements"""
                    }

                    test("matchInOrder checks size first and fails with right message (2)") {
                        {
                            list shouldMatchInOrder listOf(startWith('w'))
                        } shouldFailWithMessage
                                """"[cat, dog]" has 2 elements while it should have 1 element"""
                    }
                }
                group("match in any order") {
                    list shouldMatchInAnyOrder listOf(
                            equal("dog"),
                            equal("cat")

                    )
                }
                group("all match") {
                    test("should all match") {
                        list shouldAll match { it.length == 3 }
                    }

                    test("should all match fails with right message") {
                        { list shouldAll match("have a size of 100") { it.length == 100 } } shouldFailWithMessage
                                """"[cat, dog]" does not matcher all matcher:
                                   | * @0: "cat" does not have a size of 100
                                   | * @1: "dog" does not have a size of 100""".trimMargin()
                    }
                }
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

                group("check message") {
                    it("can check an exception message") {
                        failing shouldFailWithMessage "Index: 0, Size: 0"
                    }

//                    it("checking an exception message fails with right message") {
//                        {failing shouldFailWithMessage "that's not the right message"} shouldFailWithMessage
//                                """thrown exception should have message: "that's not the right message""""
//                    }

                    it("can check an exception message with a custom matcher") {
                        failing shouldFailWithAMessageThat equal("Index: 0, Size: 0")
                    }

//                    it("checking an exception message with a custom matcher should fail with right message") {
//                        { failing shouldFailWithAMessageThat match("have a size of 1 char") { it.length == 1 } } shouldFailWithMessage
//                                """thrown exception should have a message matching: "have a size of 1 char""""
//                    }
                }

                group("with matcher") {
                    it("should pass `failing shouldThrow IndexOutOfBoundsException::class that match { it.message != null }`") {
                        failing shouldThrow IndexOutOfBoundsException::class that match { it.message != null }
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
                        failingTest shouldThrow AssertionError::class
                    }

                    it("should throw the right message") {
                        failingTest shouldFailWithMessage """"kitty" does not match an undescribed criteria"""
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
                    assertEquals(""""kitty" does not have a size of 3""", failMessage)
                }

                test("you can specify the error cause with `but` function") {
                    {
                        "kitty" should (match<String>("have a size of 3") { it.length == 3 }
                                but { "it has a size of ${it.length}" })
                    } shouldFailWithMessage
                            """"kitty" does not have a size of 3: it has a size of 5"""
                }
            }

            group("with your own matcher") {
                class SizeOfMatcher(private val expectedSize: Int)
                    : Matcher<String> {
                    override val description = "has a size of $expectedSize"
                    override fun match(actual: String) =
                            if (actual.length == expectedSize) MatchResult.ok
                            else fail("$actual has not a size of $expectedSize")
                }

                it("it can pass") {
                    "dog" should SizeOfMatcher(3)
                }

                it("it can fail with your message") {
                    { "dog" should SizeOfMatcher(2) } shouldFailWithMessage "dog has not a size of 2"
                }

            }
        }

        group("generic list treatment") {

        }
    }

})
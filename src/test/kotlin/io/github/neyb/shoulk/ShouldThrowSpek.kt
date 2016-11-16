package io.github.neyb.shoulk

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import java.util.*

class ShouldThrowSpek : Spek({
    given("a function throwing an illegalstateexception") {
        val failing = { ArrayList<String>().get(0) }

        it("should pass `shouldThrow okMatcher`") {
            failing shouldThrow { e: Exception -> e is IndexOutOfBoundsException }
        }

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
    }
})
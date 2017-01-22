package io.github.neyb.shoulk.util

import io.github.neyb.shoulk.shouldEqual
import org.jetbrains.spek.api.Spek

class IndexesSpek : Spek({
    group("remove duplicates") {
        group("on singletons") {
            test("(1),(2),(3) => (1),(2),(3)") {
                listOf(setOf(1), setOf(2), setOf(3)).removeDuplicateIndexes() shouldEqual
                        listOf(setOf(1), setOf(2), setOf(3))
            }

            test("(1),(1) => (1),()") {
                listOf(setOf(1), setOf(1)).removeDuplicateIndexes() shouldEqual
                        listOf(setOf(1), setOf())
            }

            test("(1),(2),(2) => (1),(2),()") {
                listOf(setOf(1), setOf(2), setOf(2)).removeDuplicateIndexes() shouldEqual
                        listOf(setOf(1), setOf(2), setOf())
            }

            test("(1),(2),(1) => (1),(2),()") {
                listOf(setOf(1), setOf(2), setOf(1)).removeDuplicateIndexes() shouldEqual
                        listOf(setOf(1), setOf(2), setOf())
            }
        }

        test("(1,2),(1,2),(1) => [2], [], [1]") {
            listOf(setOf(1, 2), setOf(1, 2), setOf(1)).removeDuplicateIndexes() shouldEqual
                    listOf(setOf(2), setOf(), setOf(1))
        }

        test("(1,2),(1,2),(1,2) => [1,2], [1,2], []") {
            listOf(setOf(1, 2), setOf(1, 2), setOf(1, 2)).removeDuplicateIndexes() shouldEqual
                    listOf(setOf(1, 2), setOf(1, 2), setOf())
        }

        test("(1,2,3),(1,2,3),(1,2,3),(1,2) => (1,2,3),(1,2,3),(1,2,3),()") {
            listOf(setOf(1, 2, 3), setOf(1, 2, 3), setOf(1, 2, 3), setOf(1, 2)).removeDuplicateIndexes() shouldEqual
                    listOf(setOf(1, 2, 3), setOf(1, 2, 3), setOf(1, 2, 3), setOf())
        }
    }

    group("reverse") {
        test("(0),(1),(2) => (0),(1),(2)") {
            listOf(setOf(0), setOf(1), setOf(2)).reverse() shouldEqual
                    listOf(setOf(0), setOf(1), setOf(2))
        }

        test("(1),(2),(0) => (2),(0),(1)") {
            listOf(setOf(1), setOf(2), setOf(0)).reverse() shouldEqual
                    listOf(setOf(2), setOf(0), setOf(1))
        }

        test("(),(0,1,2),() => (1),(1),(1)") {
            listOf(setOf(), setOf(0, 1, 2), setOf()).reverse() shouldEqual
                    listOf(setOf(1), setOf(1), setOf(1))
        }

        test("(),(),(0,1,2) => (2),(2),(2)") {
            listOf(setOf(), setOf(), setOf(0, 1, 2)).reverse() shouldEqual
                    listOf(setOf(2), setOf(2), setOf(2))
        }
        test("(0),(0,1),(2) => (0,1),(1),(2)") {
            listOf(setOf(0), setOf(0,1), setOf(2)).reverse() shouldEqual
                    listOf(setOf(0,1), setOf(1), setOf(2))
        }
    }
})
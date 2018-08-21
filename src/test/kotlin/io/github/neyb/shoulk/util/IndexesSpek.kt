package io.github.neyb.shoulk.util

import io.github.neyb.shoulk.shouldEqual
import io.kotlintest.specs.FreeSpec

class IndexesSpek : FreeSpec({
    "remove duplicates" - {
        "on singletons" - {
            "(1),(2),(3) => (1),(2),(3)" {
                listOf(setOf(1), setOf(2), setOf(3)).removeDuplicateIndexes() shouldEqual
                        listOf(setOf(1), setOf(2), setOf(3))
            }

            "(1),(1) => (1),()" {
                listOf(setOf(1), setOf(1)).removeDuplicateIndexes() shouldEqual
                        listOf(setOf(1), setOf())
            }

            "(1),(2),(2) => (1),(2),()" {
                listOf(setOf(1), setOf(2), setOf(2)).removeDuplicateIndexes() shouldEqual
                        listOf(setOf(1), setOf(2), setOf())
            }

            "(1),(2),(1) => (1),(2),()" {
                listOf(setOf(1), setOf(2), setOf(1)).removeDuplicateIndexes() shouldEqual
                        listOf(setOf(1), setOf(2), setOf())
            }
        }

        "(1,2),(1,2),(1) => [2], [], [1]" {
            listOf(setOf(1,2), setOf(1,2), setOf(1)).removeDuplicateIndexes() shouldEqual
                    listOf(setOf(2), setOf(), setOf(1))
        }

        "(1,2),(1,2),(1,2) => [1,2], [1,2], []" {
            listOf(setOf(1,2), setOf(1,2), setOf(1,2)).removeDuplicateIndexes() shouldEqual
                    listOf(setOf(1,2), setOf(1,2), setOf())
        }

        "(1,2,3),(1,2,3),(1,2,3),(1,2) => (1,2,3),(1,2,3),(1,2,3),()" {
            listOf(setOf(1,2,3), setOf(1,2,3), setOf(1,2,3), setOf(1,2)).removeDuplicateIndexes() shouldEqual
                    listOf(setOf(1,2,3), setOf(1,2,3), setOf(1,2,3), setOf())
        }
    }
})
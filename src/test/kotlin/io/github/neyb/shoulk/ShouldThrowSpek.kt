package io.github.neyb.shoulk

import org.jetbrains.spek.api.Spek
import java.util.*

class ShouldThrowSpek : Spek({
    test("test1") {
        val failing = { ArrayList<String>().get(0) }
        failing shouldThrow ({ e:Exception -> e is IndexOutOfBoundsException})
    }
})
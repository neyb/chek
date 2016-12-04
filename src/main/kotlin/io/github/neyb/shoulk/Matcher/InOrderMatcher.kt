package io.github.neyb.shoulk.Matcher

class InOrderMatcher<in T>(
        val matchers: List<Matcher<T>>) : Matcher<Iterable<T>> {
    override val description = "match matchers"

    override fun match(actual: Iterable<T>): MatchResult {
        var index = -1
        val iterator = actual.iterator()
        while (iterator.hasNext()) {
            ++index
            val curr = iterator.next()
            if (matchers.size < index + 1) {
                return hasDifferentSize(actual, index + 1 + iterator.count())
            }
            val matchResult = matchers[index].match(curr)
            if (!matchResult.success)
                return (matchResult as MatchResult.Fail).wrap("\"$actual\" does not match matchers")
        }
        if (matchers.size != index + 1) return hasDifferentSize(actual, index+1)
        return MatchResult.ok
    }

    private fun hasDifferentSize(actual: Iterable<T>, actualSize: Int): MatchResult.Fail {
        return MatchResult.Fail("\"$actual\" has ${getNElementsLabel(actualSize)} while it should have ${getNElementsLabel(matchers.size)}")
    }

    private fun getNElementsLabel(n: Int) = "$n ${if (n>1) "elements" else "element"}"

    private fun Iterator<*>.count(): Int {
        var index = 0
        this.forEach { ++index }
        return index
    }
}
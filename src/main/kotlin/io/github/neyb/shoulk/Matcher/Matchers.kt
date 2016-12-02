package io.github.neyb.shoulk.Matcher

fun <T> equal(expected: T) = match<T>("""be equals to "$expected"""") { it == expected }

fun <T> sameAs(expected: T) = match<T>("""be the same object as "$expected"@${System.identityHashCode(expected)}""")
{ it === expected } but { "its identity hashCode is @${System.identityHashCode(it)}" }

fun <T> contain(expected: T) = match<Iterable<T>>("""contain "$expected"""") { it.contains(expected) }

fun <T> contain(matcher: Matcher<T>) = match<Iterable<T>>("""contain an element matching "${matcher.description}"""")
{ it.any { matcher.match(it) } }

//fun <T> matchInOrder(matchers: List<Matcher<T>>) = match<Iterable<T>>("") {
//    if(it.size() != matchers.size)
//        throw
//    for (curr in it.withIndex()) {
//        if(matchers.size < curr.index+1) throw AssertionError("")
//    }
//
//    return true;
//
//    fun Iterable<T>.size():Int {
//        var count = 0
//        this.forEach { ++count }
//        return count
//    }
//
//}

fun <T> allMatch(matcher: Matcher<T>): Matcher<Iterable<T>> = AllMatcher(matcher)

fun <T> noneMatch(matcher: Matcher<T>): Matcher<Iterable<T>> = NoneMatcher(matcher)

private fun Iterable<*>.size() : Int {
    var size = 0
    this.forEach { ++size }
    return size
}
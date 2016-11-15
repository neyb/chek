package io.github.neyb.tchekik.matcher

//fun <T> described(messageBuilder: (T) -> String, matcher: (T) -> Boolean) =
//        SimpleMatcher(messageBuilder, matcher)

class SimpleMatcher<T>(
        private val matcher: (T) -> Boolean,
        private val messageBuilder: (T) -> String)
    : Matcher<T> {
    override fun match(actual: T) = matcher(actual)
    override fun assertionErrorMessage(actual: T) = messageBuilder(actual)
}

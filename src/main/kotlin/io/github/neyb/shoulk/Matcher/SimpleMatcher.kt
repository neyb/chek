package io.github.neyb.shoulk.Matcher

infix fun <T> ((T) -> Boolean).describeFailure(failureDescriptionBuilder: (T) -> String) =
        SimpleMatcher(this, failureDescriptionBuilder)

class SimpleMatcher<T>(
        private val criteria: (T) -> Boolean,
        private val failureDescriptionBuilder: (T) -> String)
    : Matcher<T> {

    override fun match(actual: T) = criteria(actual)

    override fun getDismatchDescriptionFor(actual: T) = failureDescriptionBuilder(actual)
}

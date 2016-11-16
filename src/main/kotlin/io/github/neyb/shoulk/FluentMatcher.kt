package io.github.neyb.shoulk

infix fun <T> ((T) -> Boolean).describedAs(description: String) = FluentMatcher(this, description)

class FluentMatcher<T>(
        private val matcher: (T) -> Boolean,
        private val description: String = "match a not described criteria",
        private val dismatchDescriptionBuilder: ((T) -> String)? = null,
        private val positive: Boolean = true
) : Matcher<T> {

    override fun match(actual: T) = matcher(actual)

    override fun assertionErrorMessage(actual: T) =
            if (dismatchDescriptionBuilder == null) expectedDescription(actual)
            else "${expectedDescription(actual)} but ${dismatchDescription(actual, dismatchDescriptionBuilder)}"

    infix fun but(dismatchDescriptionBuilder: ((T) -> String)) = FluentMatcher(matcher, description, dismatchDescriptionBuilder, positive)

    operator fun not() = FluentMatcher({ actual -> !matcher.invoke(actual) }, description, dismatchDescriptionBuilder, !positive)

    private fun expectedDescription(actual: T) = """"$actual" ${if (positive) "should" else "should not"} $description"""

    private fun dismatchDescription(actual: T, dismatchDescriptionBuilder: (T) -> String) = dismatchDescriptionBuilder.invoke(actual)
}
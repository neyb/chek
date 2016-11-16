package io.github.neyb.shoulk.matcher

infix fun <T> ((T)->Boolean).describedAs(description: String) = FluentMatcher(this, description)

class FluentMatcher<T>(
        private val matcher: (T) -> Boolean,
        private val description: String = "should match a not described criteria",
        private val dismatchDescriptionBuilder: ((T) -> String)? = null
) : Matcher<T> {
    override fun match(actual: T) = matcher(actual)

    override fun assertionErrorMessage(actual: T) =
            if (dismatchDescriptionBuilder == null) expectedDescription(actual)
            else "${expectedDescription(actual)} but ${dismatchDescription(actual, dismatchDescriptionBuilder)}"

    private fun expectedDescription(actual: T) = """"$actual" $description"""

    private fun dismatchDescription(actual: T, dismatchDescriptionBuilder: (T) -> String) = dismatchDescriptionBuilder.invoke(actual)

    infix fun but(dismatchDescriptionBuilder: ((T) -> String)) = FluentMatcher(matcher, description, dismatchDescriptionBuilder)
}
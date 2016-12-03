package io.github.neyb.shoulk.Matcher

fun <T> match(matcher: (T) -> Boolean): FluentMatcher<T> = FluentMatcher("match an undescribed criteria", matcher = matcher)
fun <T> match(description: String, matcher: (T) -> Boolean) = FluentMatcher(description, matcher = matcher)

class FluentMatcher<T> internal constructor(
        override val description: String,
        private val dismatchDescriptionBuilder: ((T) -> String)? = null,
        private val positive: Boolean = true,
        private val butLabel: String = "but",
        private val matcher: (T) -> Boolean
) : Matcher<T> {

    override fun match(actual: T) = matcher(actual)

    override fun getDismatchDescriptionFor(actual: T) =
            if (dismatchDescriptionBuilder == null) expectedDescription(actual)
            else "${expectedDescription(actual)} $butLabel ${dismatchDescription(actual, dismatchDescriptionBuilder)}"

    infix fun but(dismatchDescriptionBuilder: ((T) -> String)) = copy(dismatchDescriptionBuilder = dismatchDescriptionBuilder)

    infix fun butFailBecause(dismatchDescriptionBuilder: ((T) -> String)) =
            copy(butLabel = "but it fails because", dismatchDescriptionBuilder = dismatchDescriptionBuilder)

    operator fun not() = copy(positive = !positive, matcher = { actual -> !matcher.invoke(actual) })

    private fun expectedDescription(actual: T) = """"$actual" ${if (positive) "should" else "should not"} $description"""

    private fun dismatchDescription(actual: T, dismatchDescriptionBuilder: (T) -> String) = dismatchDescriptionBuilder.invoke(actual)

    private fun copy(
            description: String = this.description,
            dismatchDescriptionBuilder: ((T) -> String)? = this.dismatchDescriptionBuilder,
            positive: Boolean = this.positive,
            butLabel: String = this.butLabel,
            matcher: (T) -> Boolean = this.matcher
    ) = FluentMatcher(description, dismatchDescriptionBuilder, positive, butLabel, matcher)
}
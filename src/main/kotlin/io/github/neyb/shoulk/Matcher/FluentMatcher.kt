package io.github.neyb.shoulk.Matcher

fun <T> match(matcher: (T) -> Boolean) = FluentMatcher("match an undescribed criteria", matcher = matcher)
fun <T> match(description: String, matcher: (T) -> Boolean) = FluentMatcher(description, matcher = matcher)

private val firstWorldSplitter = Regex("""\w+(.*)""")

class FluentMatcher<T> internal constructor(
        override val description: String,
        private val dismatchDescriptionBuilder: ((T) -> String)? = null,
        private val positive: Boolean = true,
        private val matcher: (T) -> Boolean
) : SimpleMatcher<T>(description) {

    override fun doesMatch(actual: T) = matcher(actual)

    override fun getDismatchDescriptionFor(actual: T) =
            if (dismatchDescriptionBuilder == null) expectedDescription(actual)
            else expectedDescription(actual) + ": " + dismatchDescription(actual, dismatchDescriptionBuilder)

    infix fun but(dismatchDescriptionBuilder: ((T) -> String)) = copy(dismatchDescriptionBuilder = dismatchDescriptionBuilder)

    operator fun not() = copy(positive = !positive, matcher = { actual -> !matcher.invoke(actual) })

    private fun expectedDescription(actual: T) = """"$actual" ${conjuguate(!positive, description)}"""

    private fun dismatchDescription(actual: T, dismatchDescriptionBuilder: (T) -> String) = dismatchDescriptionBuilder.invoke(actual)

    private fun copy(
            description: String = this.description,
            dismatchDescriptionBuilder: ((T) -> String)? = this.dismatchDescriptionBuilder,
            positive: Boolean = this.positive,
            matcher: (T) -> Boolean = this.matcher
    ) = FluentMatcher(description, dismatchDescriptionBuilder, positive, matcher)

    private fun conjuguate(positive: Boolean, description: String) = when{
        description.startsWith("be", true) ->
            if(positive) "is"+exceptFirstWord(description)
            else "is not"+exceptFirstWord(description)
        else ->
            if(positive) "does $description"
            else "does not $description"
    }

    private fun exceptFirstWord(phrase: String) =
            firstWorldSplitter.find(phrase)?.groupValues?.get(1)?:""
}
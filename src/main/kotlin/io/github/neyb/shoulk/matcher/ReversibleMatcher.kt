package io.github.neyb.shoulk.matcher

interface ReversibleMatcher<in T, out R: ReversibleMatcher<T, R>> : Matcher<T> {
    operator fun not():R
}
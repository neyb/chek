package io.github.neyb.shoulk.Matcher

interface ReversibleMatcher<in T, out R: ReversibleMatcher<T, R>> : Matcher<T> {
    operator fun not():R
}
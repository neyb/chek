package io.github.neyb.tchekik.matcher

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match

interface Matcher<in T> {
    companion object Factory

    fun match(actual:T):Boolean
    fun assertionErrorMessage(actual:T):String
}
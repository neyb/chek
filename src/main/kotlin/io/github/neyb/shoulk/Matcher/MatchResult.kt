package io.github.neyb.shoulk.Matcher

sealed class MatchResult (val success:Boolean){
    abstract fun check()

    companion object ok : MatchResult(true) {
        override fun check(){}
    }

    class Fail(val errorMessage:String) :MatchResult(false) {
        override fun check() = throw AssertionError(errorMessage)
        fun wrap(wrapErrorMessage:String) = Fail("$wrapErrorMessage: $errorMessage")
    }
}

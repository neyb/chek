package io.github.neyb.shoulk.util

import java.util.*

internal fun List<Set<Int>>.removeDuplicateIndexes():List<Set<Int>> {
    val deduplicates = this.map { it.toMutableSet() }
    removeDuplicates(deduplicates)
    return deduplicates.map { it.toSet() }
}

internal fun List<Set<Int>>.reverse(): List<Set<Int>> {
    val result = ArrayList<MutableSet<Int>>()
    kotlin.repeat(size) {result.add(HashSet())}
    asSequence()
            .withIndex()
            .flatMap { indexed -> indexed.value.asSequence().map { IndexedValue(indexed.index, it) } }
            .forEach { result[it.value].add(it.index) }

    return result
}

private fun removeDuplicates(toDeduplicate: List<MutableSet<Int>>){
    if (toDeduplicate.isEmpty()) return
    val maxSetSize = toDeduplicate.asSequence().map { it.size }.max()!!
    var hasChange = true
    while (hasChange) {
        hasChange = false
        for (currentSizeCheck in 1..maxSetSize) {
            getOnlyPositionsToKeepIndexByIndexForSize(currentSizeCheck, toDeduplicate).forEach { indexToRemove, onlyPositionToKeep ->
                for ((currentIndex, indexes) in toDeduplicate.withIndex()) {
                    if (!onlyPositionToKeep.contains(currentIndex)) {
                        hasChange = indexes.remove(indexToRemove) || hasChange
                    }
                }
            }
        }
    }
}

private fun getOnlyPositionsToKeepIndexByIndexForSize(currentSizeCheck: Int, toDeduplicate: List<Set<Int>>): HashMap<Int, Set<Int>> {
    val foundTimesByIndexes = HashMap<Set<Int>, Int>()
    val positionsToKeepIndexByIndexes = HashMap<Set<Int>, MutableSet<Int>>()
    val onlyPositionsToKeepIndexByIndex = HashMap<Int, Set<Int>>()
    for ((indexInInput, indexes) in toDeduplicate.withIndex()) {
        if (indexes.size == currentSizeCheck) {
            var alreadyFoundTimes = foundTimesByIndexes[indexes]?:0
            if (alreadyFoundTimes < currentSizeCheck){
                val positionsToKeepIndex = positionsToKeepIndexByIndexes.compute(indexes) { k, v ->
                    if (v == null)
                        mutableSetOf(indexInInput)
                    else {
                        v.add(indexInInput)
                        v
                    }
                }
                alreadyFoundTimes = foundTimesByIndexes.merge(indexes, 1) { presentValue, one -> presentValue + one}
                if (alreadyFoundTimes == currentSizeCheck) {
                    indexes.forEach {
                        onlyPositionsToKeepIndexByIndex[it] = positionsToKeepIndex
                    }
                }
            }
        }
    }
    return onlyPositionsToKeepIndexByIndex
}

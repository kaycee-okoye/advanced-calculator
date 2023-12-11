package com.oaktech.advancedcalculator.extensions

/**
 * Removes values in an index range from the list, and puts a new value in place of [startIndex]
 *
 * Specifically, [startIndex] will be replaced with [newValue], and [startIndex]+1 to [stopIndex]
 * (inclusive) will be removed.
 *
 * @param startIndex the index of the first value in the range to be removed
 * @param stopIndex the index of the last value in the range to be removed
 * @param newValue the value to insert at [startIndex]
 *
 * @return updated list
 */
fun replaceSublist(
    originalList: MutableList<String>,
    startIndex: Int,
    stopIndex: Int,
    newValue: String
): MutableList<String> {
    originalList[startIndex] = newValue
    return originalList.filterIndexed { index, _ -> index < startIndex + 1 || index > stopIndex }
        .toMutableList()
}
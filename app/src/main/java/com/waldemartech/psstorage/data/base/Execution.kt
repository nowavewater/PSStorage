package com.waldemartech.psstorage.data.base

import kotlinx.coroutines.delay


sealed class DelayTime {
    data class Fixed(val time: Long) : DelayTime() {
        override fun onDelay() = time
    }

    data class Range(val range: LongRange) : DelayTime() {
        override fun onDelay() = range.random()
    }

    abstract fun onDelay() : Long
}

suspend inline fun random(
    delayTime: DelayTime = DelayTime.Range(1000L..10000L),
    execution: () -> Unit
) {
    delay(delayTime.onDelay())
    execution()
}
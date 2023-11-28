package com.liike.liikegomi.background.utils

import kotlin.random.Random
import kotlin.random.nextLong

object RandomUtils {
    fun getSimulationSeconds(): Long {
        return Random.nextLong(1000, 5000)
    }
}
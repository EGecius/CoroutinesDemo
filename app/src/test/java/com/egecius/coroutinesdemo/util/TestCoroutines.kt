package com.egecius.coroutinesdemo.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun neverEndingFlow(): Flow<Unit> {
    return flow {
        while (true) {
            delay(3000)
        }
    }
}

package com.egecius.coroutinesdemo.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun neverEndingEmptyFlow(): Flow<Unit> {
    return flow {
        while (true) {
            delay(100)
        }
    }
}

fun neverEndingEmittingFlow(): Flow<Unit> {
    return flow {
        while (true) {
            delay(10)
            emit(Unit)
        }
    }
}

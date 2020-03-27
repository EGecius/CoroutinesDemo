package com.egecius.coroutinesdemo.util

import kotlinx.coroutines.CoroutineScope

fun CoroutineScope.log(msg: String) {
    val threadName = Thread.currentThread().name
    val fullMsg = "$threadName, $coroutineContext: + $msg"
    println(fullMsg)
}
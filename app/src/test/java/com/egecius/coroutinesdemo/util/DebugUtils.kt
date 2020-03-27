package com.egecius.coroutinesdemo.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

fun CoroutineScope.log(msg: String) {
    val threadName = Thread.currentThread().name
    val job = println(coroutineContext[Job])
    val fullMsg = "$threadName, $job: + $msg"
    println(fullMsg)
}
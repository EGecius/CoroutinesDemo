package com.egecius.coroutinesdemo.util

import kotlinx.coroutines.CoroutineScope

fun CoroutineScope.log(msg: String) {
    val fullMsg = "$coroutineContext: + $msg"
    println(fullMsg)
}
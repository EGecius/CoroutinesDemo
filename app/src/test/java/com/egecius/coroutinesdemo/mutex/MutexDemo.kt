package com.egecius.coroutinesdemo.mutex

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.junit.Test
import kotlin.system.measureTimeMillis

@ExperimentalCoroutinesApi
class MutexDemo {

    private val mutex = Mutex()
    private var counter = 0

    @Test
    fun `with mutex incrementation is done correctly`() = runBlocking {

        withContext(Dispatchers.Default) {
            massiveRun {
                // protect each increment with lock
                mutex.withLock {
                    counter++
                }
            }
        }
        println("Counter = $counter")
    }
}



suspend fun massiveRun(action: suspend () -> Unit) {
    val n = 10  // number of coroutines to launch
    val k = 100 // times an action is repeated by each coroutine
    val time = measureTimeMillis {
        coroutineScope { // scope for coroutines
            repeat(n) {
                launch {
                    repeat(k) { action() }
                }
            }
        }
    }
    println("Completed ${n * k} actions in $time ms")
}

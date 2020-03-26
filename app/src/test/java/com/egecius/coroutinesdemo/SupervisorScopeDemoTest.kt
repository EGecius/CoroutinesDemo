package com.egecius.coroutinesdemo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class SupervisorScopeDemoTest {

    @Test(expected = Exception::class)
    fun `when a child coroutine in supervisor scope fails, siblings don't get cancelled`() = runBlockingTest {

        launch {
            supervisorScope {
                println("starting child1")

                val child1 = launch {
                    delay(100)
                    throw Exception("Egis")
                }

                val child2 = launch {
                    println("starting child2")
                    delay(100)
                    println("completed child2")
                }
            }
        }
    }

    @Test(expected = Exception::class)
    fun `without supervisor scope, siblings get cancelled on failure`() = runBlockingTest {

        launch {
            println("starting child1")

            val child1 = launch {
                delay(100)
                throw Exception("Egis")
            }

            val child2 = launch {
                println("starting child2")
                delay(100)
                println("completed child2")
            }
        }
    }
}
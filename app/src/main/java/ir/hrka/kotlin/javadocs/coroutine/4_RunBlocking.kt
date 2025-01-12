package ir.hrka.kotlin.javadocs.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/**
 * * runBlocking create a coroutine scope.
 * * runBlocking is also a coroutine builder that bridges the non-coroutine world of a regular fun and the code with coroutines inside of runBlocking curly braces.
 * * The name of runBlocking means that the thread that runs it gets blocked for the duration of the call, until all the coroutines inside runBlocking complete their execution.
 * * You will often see runBlocking used like that at the very top-level of the application and quite rarely inside the real code.
 *    * Threads are expensive resources and blocking them is inefficient and is often not desired.
 *    * Frequently used in test environments to ensure synchronous execution of coroutine-based code.
 * * In the following codes, i run some task by runBlocking in the main thread.
 *    * When it comes to runBlocking, the main thread remains blocked until its children complete execution.
 *    * The tasks in the runBlocking execute in a coroutine.
 *    * The tasks in the runBlocking can execute in two separate coroutine concurrently.
 * ```
 *     fun main() {
 *         task1()
 *
 *         runBlocking {
 *             task2()
 *             task3()
 *         }
 *
 *         task4()
 *     }
 *
 *     private fun task1() { ... }
 *     private suspend fun task2() { ... }
 *     private suspend fun task3() { ... }
 *     private fun task4() { ... }
 * ```
 */

class RunBlocking {

    fun main() {
        task1()

        runBlocking {
            task2()
            task3()
        }

        task4()
    }


    private fun task1() {
        Thread.sleep(1_000)
    }

    private suspend fun task2() {
        delay(4_000)
    }

    private suspend fun task3() {
        delay(2_000)
    }

    private fun task4() {
        Thread.sleep(3_000)
    }
}
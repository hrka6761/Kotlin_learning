package ir.hrka.kotlin.javadocs.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * * In the Concurrent programming, we can serve the Coroutines To execute multi task simultaneously and independently of each other.
 * * Coroutines = cooperative routines (cooperative functions).
 * * Coroutines are lightweight, user-level constructs managed by the programming language runtime.
 * * Coroutines can be thought of as light-weight threads (Coroutines are less resource-intensive than JVM threads).
 *    * They are much more memory-efficient, often requiring only a small memory overhead for their stack.
 *    * You can have thousands (or even millions) of coroutines running concurrently.
 * * Coroutines are non-blocking and suspend execution voluntarily without kernel intervention.
 * * Coroutines run within threads and don’t achieve parallelism (parallel programming) unless explicitly distributed across threads.
 * * Coroutines may not be available in all languages (e.g., native support in Kotlin, Python, etc.).
 * * Coroutines follow a principle of structured concurrency which means that new coroutines can only be launched in a specific CoroutineScope which delimits the lifetime of the coroutine.
 *    * Structured concurrency ensures that coroutines are not lost and do not leak.
 *    * Outer scope cannot complete until all its children coroutines complete.
 *    * Structured concurrency also ensures that any errors in the code are properly reported and are never lost.
 * * suspending function
 *    * They can only be used inside coroutines.
 *    * We can use other suspending functions in this functions (like delay in this example) to suspend execution of a coroutine.
 * ```
 * suspend fun suspendingFunction() { ... }
 * ```
 * * In the following codes, i run some task in another Coroutine but in the main thread.
 * ```
 *     fun main() {
 *         task1()
 *
 *         CoroutineScope(Dispatchers.Main).launch {
 *             task2()
 *             task3()
 *         }
 *         CoroutineScope(Dispatchers.Main).launch {
 *             task4()
 *             task5()
 *         }
 *
 *         task6()
 *     }
 *
 *     private fun task1() { ... }
 *     private suspend fun task2() { ... }
 *     private suspend fun task3() { ... }
 *     private suspend fun task4() { ... }
 *     private suspend fun task5() { ... }
 *     private fun task6() { ... }
 * ```
 */

class Coroutines {

    fun main() {
        task1()

        CoroutineScope(Dispatchers.Main).launch {
            task2()
            task3()
        }

        CoroutineScope(Dispatchers.Main).launch {
            task4()
            task5()
        }

        task6()
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

    private suspend fun task4() {
        delay(3_000)
    }

    private suspend fun task5() {
        delay(1_000)
    }

    private fun task6() {
        Thread.sleep(2_500)
    }
}
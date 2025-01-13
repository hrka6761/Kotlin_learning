package ir.hrka.kotlin.javadocs.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * * The coroutineScope function is a suspending function that creates a new coroutine scope.
 *    * It ensures that all of them complete before the coroutineScope itself completes.
 *    * It can only be called from within another coroutine or suspending function.
 *    * If any coroutine within the scope fails, the scope itself fails, and all coroutines within it are cancelled.
 * * Coroutines follow a principle of structured concurrency which means that new coroutines can only be launched in a specific coroutine Scope which delimits the lifetime of the coroutine.
 * * runBlocking and coroutineScope functions may look similar because they both wait for their body and all its children to complete.
 *    * runBlocking is a regular function and coroutineScope is a suspending function.
 *    * the runBlocking method blocks the current thread for waiting, while coroutineScope just suspends, releasing the underlying thread for other usages (suspend coroutine).
 * * In the following codes, i run some task by coroutineScope function in the main thread.
 * ```
 *     fun main() {
 *         task1()
 *
 *         CoroutineScope(Dispatchers.Main).launch {
 *             task2()
 *
 *             coroutineScope {
 *                 launch {
 *                     task3()
 *                 }
 *                 launch {
 *                     task4()
 *                 }
 *             }
 *
 *             task5()
 *         }
 *
 *         task6()
 *     }
 *
 *
 *     private fun task1() { ... }
 *     private suspend fun task2() { ... }
 *     private suspend fun task3() { ... }
 *     private suspend fun task4() { ... }
 *     private suspend fun task5() { ... }
 *     private fun task6() { ... }
 * ```
 */

class CoroutineScopeFunction {

    fun main() {
        task1()

        CoroutineScope(Dispatchers.Main).launch {
            task2()

            coroutineScope {
                launch {
                    task3()
                }
                launch {
                    task4()
                }
            }

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
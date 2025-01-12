package ir.hrka.kotlin.javadocs.coroutine

/**
 * * In the Concurrent programming, we can serve the threads To execute multi task simultaneously and independently of each other.
 * * Threads are a low-level construct managed by the operating system.
 * * Threads can run on multiple CPU cores, leveraging hardware parallelism (Parallel programming).
 * * Threads are available in virtually all programming languages and operating systems.
 * * In the use of threads, the following should be considered:
 *    * Creating and managing threads is resource-intensive.
 *    * Threads require careful synchronization to avoid race conditions, deadlocks, and other concurrency issues.
 * * In the following codes, i run some task in another threads.
 *    * Tasks in separate threads (task2, task3, task4, and task5) execute concurrently with tasks (task1, task6) in the main thread.
 *    * Thread execution order is non-deterministic, meaning the exact sequence of task2, task3, task4, task5, and task6 depends on the thread scheduler.
 * ```
 *     fun main() {
 *         task1()
 *
 *         Thread {
 *             task2()
 *             task3()
 *         }.start()
 *
 *         Thread {
 *             task4()
 *             task5()
 *         }.start()
 *
 *         task6()
 *     }
 *
 *     private fun task1() { ... }
 *     private fun task2() { ... }
 *     private fun task3() { ... }
 *     private fun task4() { ... }
 *     private fun task5() { ... }
 *     private fun task6() { ... }
 * ```
 */

class MultiThreadProgramming {

    fun main() {
        task1()

        Thread {
            task2()
            task3()
        }.start()

        Thread {
            task4()
            task5()
        }.start()

        task6()
    }


    private fun task1() {
        Thread.sleep(1_000)
    }

    private fun task2() {
        Thread.sleep(4_000)
    }

    private fun task3() {
        Thread.sleep(2_000)
    }

    private fun task4() {
        Thread.sleep(3_000)
    }

    private fun task5() {
        Thread.sleep(1_000)
    }

    private fun task6() {
        Thread.sleep(2_500)
    }
}
package ir.hrka.kotlin.javadocs.coroutine

import ir.hrka.kotlin.core.utilities.Log.printYellow

/**
 * * In the Concurrent programming, we can serve the threads To execute multi task simultaneously and independently of each other.
 * * Threads are a low-level construct managed by the operating system.
 * * Threads can run on multiple CPU cores, leveraging hardware parallelism (Parallel programming).
 * * Threads are available in virtually all programming languages and operating systems.
 * * In the use of threads, the following should be considered:
 *    * Creating and managing threads is resource-intensive.
 *    * Threads require careful synchronization to avoid race conditions, deadlocks, and other concurrency issues.
 */

class MultiThreadProgramming {

    fun fun1() {
        task1()
        task2()
        task3()
    }


    private fun task1() {
        Thread {
            printYellow("Start task1")
            Thread.sleep(1_000)
            printYellow("End task1")
        }.start()
    }

    private fun task2() {
        Thread {
            printYellow("Start task2")
            Thread.sleep(4_000)
            printYellow("End task2")
        }.start()
    }

    private fun task3() {
        Thread {
            printYellow("Start task3")
            Thread.sleep(2_000)
            printYellow("End task3")
        }.start()
    }
}
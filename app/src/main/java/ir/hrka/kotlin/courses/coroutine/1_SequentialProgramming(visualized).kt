package ir.hrka.kotlin.courses.coroutine

import ir.hrka.kotlin.core.utilities.Log.printYellow

/**
 * * All type of programming paradigms is:
 *    * Sequential programming
 *    * Concurrent programming
 *    * Parallel programming
 * * Sequential programming is a programming paradigm where instructions are executed one after another in a specific:
 *    * In the Sequential programming instructions are executed in the single thread.
 * ```
 *    fun main() {
 *        task1()
 *        task2()
 *        task3()
 *    }
 *
 *    private fun task1() { ... }
 *    private fun task2() { ... }
 *    private fun task3() { ... }
 * ```
 */

class SequentialProgramming {

    fun fun1() {
        task1()
        task2()
        task3()
    }


    private fun task1() {
        printYellow("Start task1")
        Thread.sleep(1_000)
        printYellow("End task1")
    }

    private fun task2() {
        printYellow("Start task2")
        Thread.sleep(4_000)
        printYellow("End task2")
    }

    private fun task3() {
        printYellow("Start task3")
        Thread.sleep(2_000)
        printYellow("End task3")
    }
}
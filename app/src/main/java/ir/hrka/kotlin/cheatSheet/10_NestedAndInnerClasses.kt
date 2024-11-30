package ir.hrka.kotlin.cheatSheet

import ir.hrka.kotlin.core.utilities.Log.printYellow

/**
 * * We can nest classes in classes, interfaces in classes, classes in interfaces, and interfaces in interfaces:
 * ```
 * class KotlinClass {
 *     val outerProperty = 1
 *
 *     fun outerFun() {
 *
 *     }
 *
 *     class NestedClass {
 *     }
 * }
 * ```
 * ```
 * class KotlinClass {
 *     class NestedClass { ... }
 * }
 * ```
 * ```
 * class KotlinClass {
 *     interface NestedInterface { ... }
 * }
 * ```
 * ```
 * class KotlinInterface {
 *     class NestedClass { ... }
 * }
 * ```
 * ```
 * class KotlinInterface {
 *     interface NestedInterface { ... }
 * }
 * ```
 * * A nested class marked as inner can access the members of its outer class.
 * * Inner classes carry a reference to an object of an outer class:
 * ```
 * class KotlinClass {
 *     inner class InnerClass { ... }
 * }
 * ```
 * * An inner class is like a regular nested class in java, but a nested class is like static nested class in java.
 * * A nested class in an interface is like static nested class in java.
 * * We can access the member of outer class in the inner class:
 * ```
 * class KotlinClass {
 *     val outerProperty = 1
 *
 *     fun outerFun() {
 *
 *     }
 *
 *     class NestedClass {
 *     }
 * }
 * ```
 * * We cannot access the member of outer class in the nested class.
 * * we can access an instance of outer class in the inner class but not in the nested class.
 */

class OuterClass() {

    val outerProperty = 1


    fun outerFun() {

    }


    class NestedClass {
        val nestedProperty = 1

        // We can not access to the outer member in the nested class
        fun nestedFun() {
            /*printYellow("outer property = $outerProperty")
            outerFun()*/
        }
    }


    inner class InnerClass {
        val innerProperty = 1

        fun innerFun() {
            printYellow("outer property = $outerProperty")
            outerFun()
        }
    }


    interface NestedInterface {

        class NestedClass {

        }

        interface NestedInterface {

        }
    }
}
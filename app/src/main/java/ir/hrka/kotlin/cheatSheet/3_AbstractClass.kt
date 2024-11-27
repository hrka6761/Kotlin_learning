package ir.hrka.kotlin.cheatSheet

/**
 * * Abstract class is open and public by default.
 * * We cannot instantiate from an abstract class directly.
 * * Abstract class can contain both abstract and concrete members:
 * ```
 * abstract class job constructor(val jobName: String) {
 *
 *     abstract val Salary: Long
 *     var tax: Int = 1
 *
 *     constructor(jobName: String, jobId: Int) : this(jobName) {}
 *
 *     companion object {
 *         const val JOB_COUNTRY = "Iran"
 *     }
 *
 *     fun getJobNameAndId(): String {
 *         return "jobId = $jobId - jobName = $jobName"
 *     }
 *
 *     abstract fun calculateTax()
 * }
 * ```
 * * Abstract members is open and public by default.
 * * but concrete members must be open for overriding.
 * * An abstract member can not be private.
 * * Modifier 'final' is incompatible with 'abstract' because abstract must be open to implementation.
 * * Abstract class can contain constructor but this constructor is only used to inheritance not instantiation.
 * * Abstract properties cannot be have custom getter and setter.
 */

abstract class AbstractClass constructor(val value: String) {

    abstract val abstractProperty: Int
    var concreteProperty: Int = 1

    constructor(str: String, integer: Int) : this(str) {}

    companion object {
        const val CONSTANT = "Abstract companion"
    }

    fun concreteFun() {}

    abstract fun abstractFun()
}
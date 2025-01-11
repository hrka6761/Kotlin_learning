package ir.hrka.kotlin.core.utilities

import ir.hrka.kotlin.core.Constants.POINT_SCREEN
import ir.hrka.kotlin.core.Constants.SPLASH_SCREEN
import ir.hrka.kotlin.core.Constants.KOTLIN_SCREEN
import ir.hrka.kotlin.core.Constants.HOME_SCREEN
import ir.hrka.kotlin.core.Constants.ABOUT_SCREEN
import ir.hrka.kotlin.core.Constants.MULTI_THREAD_PROGRAMMING_SCREEN
import ir.hrka.kotlin.core.Constants.SEQUENTIAL_PROGRAMMING_SCREEN


enum class Screen(private val destination: String) {

    Splash(SPLASH_SCREEN),
    Home(HOME_SCREEN),
    Topic(KOTLIN_SCREEN),
    Point(POINT_SCREEN),
    About(ABOUT_SCREEN),
    SequentialProgramming(SEQUENTIAL_PROGRAMMING_SCREEN),
    MultiThreadProgramming(MULTI_THREAD_PROGRAMMING_SCREEN);


    operator fun invoke() = destination


    fun appendArg(vararg arg: Any): String {
        return buildString {
            append(destination)

            arg.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
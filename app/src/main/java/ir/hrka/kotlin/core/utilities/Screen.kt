package ir.hrka.kotlin.core.utilities

import ir.hrka.kotlin.core.Constants.POINT_SCREEN
import ir.hrka.kotlin.core.Constants.SPLASH_SCREEN
import ir.hrka.kotlin.core.Constants.KOTLIN_TOPICS_SCREEN
import ir.hrka.kotlin.core.Constants.COROUTINE_TOPICS_SCREEN
import ir.hrka.kotlin.core.Constants.HOME_SCREEN
import ir.hrka.kotlin.core.Constants.ABOUT_SCREEN


enum class Screen(private val destination: String) {

    Splash(SPLASH_SCREEN),
    Home(HOME_SCREEN),
    KotlinTopics(KOTLIN_TOPICS_SCREEN),
    CoroutineTopics(COROUTINE_TOPICS_SCREEN),
    Point(POINT_SCREEN),
    About(ABOUT_SCREEN);


    operator fun invoke() = destination


    fun appendArg(vararg arg: String): String {
        return buildString {
            append(destination)

            arg.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
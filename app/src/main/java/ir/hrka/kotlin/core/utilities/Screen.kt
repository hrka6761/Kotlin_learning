package ir.hrka.kotlin.core.utilities

import ir.hrka.kotlin.core.Constants.KOTLIN_TOPIC_POINTS_SCREEN
import ir.hrka.kotlin.core.Constants.SPLASH_SCREEN
import ir.hrka.kotlin.core.Constants.KOTLIN_TOPICS_SCREEN
import ir.hrka.kotlin.core.Constants.COROUTINE_TOPICS_SCREEN
import ir.hrka.kotlin.core.Constants.HOME_SCREEN
import ir.hrka.kotlin.core.Constants.ABOUT_SCREEN
import ir.hrka.kotlin.core.Constants.COROUTINE_TOPIC_POINTS_SCREEN
import ir.hrka.kotlin.core.Constants.SEQUENTIAL_PROGRAMMING_SCREEN


enum class Screen(private val destination: String) {

    Splash(SPLASH_SCREEN),
    Home(HOME_SCREEN),
    KotlinTopics(KOTLIN_TOPICS_SCREEN),
    CoroutineTopics(COROUTINE_TOPICS_SCREEN),
    KotlinTopicPoints(KOTLIN_TOPIC_POINTS_SCREEN),
    CoroutineTopicPoints(COROUTINE_TOPIC_POINTS_SCREEN),
    About(ABOUT_SCREEN),
    SequentialProgramming(SEQUENTIAL_PROGRAMMING_SCREEN);


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
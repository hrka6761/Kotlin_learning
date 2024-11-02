package ir.hrka.kotlin.core.utilities

import ir.hrka.kotlin.core.utilities.Constants.SPLASH_SCREEN
import ir.hrka.kotlin.core.utilities.Constants.HOME_SCREEN


enum class Screen(private val destination: String) {

    Splash(SPLASH_SCREEN),
    Home(HOME_SCREEN);


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
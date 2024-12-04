package ir.hrka.kotlin.core.utilities

import ir.hrka.kotlin.core.Constants.POINT_SCREEN
import ir.hrka.kotlin.core.Constants.SPLASH_SCREEN
import ir.hrka.kotlin.core.Constants.CHEATSHEETS_SCREEN
import ir.hrka.kotlin.core.Constants.HOME_SCREEN
import ir.hrka.kotlin.core.Constants.PROFILE_SCREEN


enum class Screen(private val destination: String) {

    Splash(SPLASH_SCREEN),
    Home(HOME_SCREEN),
    CheatSheet(CHEATSHEETS_SCREEN),
    Point(POINT_SCREEN),
    Profile(PROFILE_SCREEN);


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
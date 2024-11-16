package ir.hrka.kotlin

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ir.hrka.kotlin.core.utilities.Constants.HOME_SCREEN_ARGUMENT_NAME
import ir.hrka.kotlin.core.utilities.Constants.POINT_SCREEN_ARGUMENT_NAME
import ir.hrka.kotlin.core.utilities.Screen.Splash
import ir.hrka.kotlin.core.utilities.Screen.Home
import ir.hrka.kotlin.core.utilities.Screen.Point
import ir.hrka.kotlin.ui.screens.cheatsheet.CheatSheetScreen
import ir.hrka.kotlin.ui.screens.home.HomeScreen
import ir.hrka.kotlin.ui.screens.splash.SplashScreen
import ir.hrka.kotlin.ui.theme.KotlinTheme

@Composable
fun AppContent() {
    val navHostController = rememberNavController()
    val activity = (LocalContext.current as MainActivity)

    KotlinTheme {
        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = navHostController,
            startDestination = Splash()
        ) {
            composable(route = Splash()) {
                SplashScreen(activity, navHostController)
            }
            composable(
                route = "${Home()}/{${HOME_SCREEN_ARGUMENT_NAME}}",
                arguments = listOf(navArgument(HOME_SCREEN_ARGUMENT_NAME) {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                val githubVersionName =
                    backStackEntry.arguments?.getString(HOME_SCREEN_ARGUMENT_NAME)
                HomeScreen(activity, navHostController, githubVersionName)
            }
            composable(
                route = "${Point()}/{${POINT_SCREEN_ARGUMENT_NAME}}",
                arguments = listOf(navArgument(POINT_SCREEN_ARGUMENT_NAME) {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                val cheatSheetFileName =
                    backStackEntry.arguments?.getString(POINT_SCREEN_ARGUMENT_NAME) ?: ""
                CheatSheetScreen(activity, navHostController, cheatSheetFileName)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppContentPreview(modifier: Modifier = Modifier) {
    AppContent()
}
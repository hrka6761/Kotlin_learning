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
import ir.hrka.kotlin.core.Constants.CHEATSHEET_SCREEN_ARGUMENT_VERSION_NAME
import ir.hrka.kotlin.core.Constants.CHEATSHEET_SCREEN_ARGUMENT_VERSION_SUFFIX
import ir.hrka.kotlin.core.Constants.HOME_SCREEN_ARGUMENT_VERSION_NAME
import ir.hrka.kotlin.core.Constants.HOME_SCREEN_ARGUMENT_VERSION_SUFFIX
import ir.hrka.kotlin.core.Constants.POINT_SCREEN_ARGUMENT_CHEATSHEET_ID
import ir.hrka.kotlin.core.Constants.POINT_SCREEN_ARGUMENT_CHEATSHEET_NAME
import ir.hrka.kotlin.core.Constants.POINT_SCREEN_ARGUMENT_CHEATSHEET_STATE_NAME
import ir.hrka.kotlin.core.utilities.Screen.Splash
import ir.hrka.kotlin.core.utilities.Screen.Home
import ir.hrka.kotlin.core.utilities.Screen.CheatSheet
import ir.hrka.kotlin.core.utilities.Screen.Point
import ir.hrka.kotlin.core.utilities.Screen.Profile
import ir.hrka.kotlin.ui.screens.profile.ProfileScreen
import ir.hrka.kotlin.ui.screens.points.PointsScreen
import ir.hrka.kotlin.ui.screens.cheatsheets.CheatSheetScreen
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
            composable(
                route = Splash()
            ) {
                SplashScreen(activity, navHostController)
            }
            composable(
                route = "${Home()}/{${HOME_SCREEN_ARGUMENT_VERSION_NAME}}/{${HOME_SCREEN_ARGUMENT_VERSION_SUFFIX}}",
                arguments = listOf(
                    navArgument(HOME_SCREEN_ARGUMENT_VERSION_NAME) { type = NavType.StringType },
                    navArgument(HOME_SCREEN_ARGUMENT_VERSION_SUFFIX) { type = NavType.StringType },
                )
            ) { backStackEntry ->
                val githubVersionName =
                    backStackEntry.arguments?.getString(HOME_SCREEN_ARGUMENT_VERSION_NAME)
                val githubVersionSuffix =
                    backStackEntry.arguments?.getString(HOME_SCREEN_ARGUMENT_VERSION_SUFFIX)
                HomeScreen(activity, navHostController, githubVersionName, githubVersionSuffix)
            }
            composable(
                route = "${CheatSheet()}/{${CHEATSHEET_SCREEN_ARGUMENT_VERSION_NAME}}/{${CHEATSHEET_SCREEN_ARGUMENT_VERSION_SUFFIX}}",
                arguments = listOf(
                    navArgument(CHEATSHEET_SCREEN_ARGUMENT_VERSION_NAME) { type = NavType.StringType },
                    navArgument(CHEATSHEET_SCREEN_ARGUMENT_VERSION_SUFFIX) { type = NavType.StringType },
                )
            ) { backStackEntry ->
                val githubVersionName =
                    backStackEntry.arguments?.getString(CHEATSHEET_SCREEN_ARGUMENT_VERSION_NAME)
                val githubVersionSuffix =
                    backStackEntry.arguments?.getString(CHEATSHEET_SCREEN_ARGUMENT_VERSION_SUFFIX)
                CheatSheetScreen(activity, navHostController, githubVersionName, githubVersionSuffix)
            }
            composable(
                route = "${Point()}/{${POINT_SCREEN_ARGUMENT_CHEATSHEET_NAME}}/{${POINT_SCREEN_ARGUMENT_CHEATSHEET_STATE_NAME}}/{${POINT_SCREEN_ARGUMENT_CHEATSHEET_ID}}",
                arguments = listOf(navArgument(POINT_SCREEN_ARGUMENT_CHEATSHEET_NAME) {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                val cheatSheetFileName =
                    backStackEntry.arguments?.getString(POINT_SCREEN_ARGUMENT_CHEATSHEET_NAME) ?: ""
                val hasContentUpdated =
                    backStackEntry.arguments?.getString(POINT_SCREEN_ARGUMENT_CHEATSHEET_STATE_NAME)
                        ?: false
                val cheatsheetId =
                    backStackEntry.arguments?.getString(POINT_SCREEN_ARGUMENT_CHEATSHEET_ID)
                        ?: "-1"
                PointsScreen(
                    activity,
                    navHostController,
                    cheatSheetFileName,
                    cheatsheetId.toInt(),
                    hasContentUpdated == "true"
                )
            }
            composable(route = Profile()) {
                ProfileScreen(activity, navHostController)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppContentPreview(modifier: Modifier = Modifier) {
    AppContent()
}
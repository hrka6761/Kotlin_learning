package ir.hrka.kotlin.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.hrka.kotlin.core.Constants.ARGUMENT_KOTLIN_TOPIC_ID
import ir.hrka.kotlin.core.Constants.ARGUMENT_KOTLIN_TOPIC
import ir.hrka.kotlin.core.Constants.ARGUMENT_KOTLIN_TOPIC_UPDATE_STATE
import ir.hrka.kotlin.core.utilities.Screen.Splash
import ir.hrka.kotlin.core.utilities.Screen.Home
import ir.hrka.kotlin.core.utilities.Screen.Topic
import ir.hrka.kotlin.core.utilities.Screen.Point
import ir.hrka.kotlin.core.utilities.Screen.About
import ir.hrka.kotlin.core.utilities.Screen.SequentialProgramming
import ir.hrka.kotlin.presentation.ui.screens.about.AboutScreen
import ir.hrka.kotlin.presentation.ui.screens.point.KotlinTopicPointsScreen
import ir.hrka.kotlin.presentation.ui.screens.topic.KotlinTopicsScreen
import ir.hrka.kotlin.presentation.ui.screens.home.HomeScreen
import ir.hrka.kotlin.presentation.ui.screens.splash.SplashScreen
import ir.hrka.kotlin.presentation.ui.screens.visualizers.sequential_programming.SequentialProgrammingScreen
import ir.hrka.kotlin.presentation.ui.theme.KotlinTheme

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
                route = Home()
            ) {
                HomeScreen(activity, navHostController)
            }
            composable(
                route = Topic()
            ) {
                KotlinTopicsScreen(activity, navHostController)
            }
            composable(
                route = Point()
            ) { backStackEntry ->

                val kotlinTopicName =
                    backStackEntry.arguments?.getString(
                        ARGUMENT_KOTLIN_TOPIC
                    ) ?: ""
                val hasContentUpdated =
                    backStackEntry.arguments?.getBoolean(
                        ARGUMENT_KOTLIN_TOPIC_UPDATE_STATE
                    ) ?: false
                val kotlinTopicId =
                    backStackEntry.arguments?.getInt(
                        ARGUMENT_KOTLIN_TOPIC_ID
                    ) ?: -1

                KotlinTopicPointsScreen(
                    activity,
                    navHostController,
                    kotlinTopicName,
                    kotlinTopicId,
                    hasContentUpdated
                )
            }
            composable(
                route = About()
            ) {
                AboutScreen(activity, navHostController)
            }
            composable(
                route = SequentialProgramming()
            ) {
                SequentialProgrammingScreen(activity, navHostController)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppContentPreview(modifier: Modifier = Modifier) {
    AppContent()
}
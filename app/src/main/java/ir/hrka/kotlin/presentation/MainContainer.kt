package ir.hrka.kotlin.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
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
import ir.hrka.kotlin.core.Constants.POINTS_SCREEN_TOPIC_ARGUMENT
import ir.hrka.kotlin.core.Constants.TAG
import ir.hrka.kotlin.core.Constants.TOPICS_SCREEN_COURSE_ARGUMENT
import ir.hrka.kotlin.core.utilities.Course
import ir.hrka.kotlin.core.utilities.Course.Kotlin
import ir.hrka.kotlin.core.utilities.Screen.Splash
import ir.hrka.kotlin.core.utilities.Screen.Home
import ir.hrka.kotlin.core.utilities.Screen.Topic
import ir.hrka.kotlin.core.utilities.Screen.Point
import ir.hrka.kotlin.core.utilities.Screen.About
import ir.hrka.kotlin.core.utilities.Screen.SequentialProgramming
import ir.hrka.kotlin.core.utilities.Screen.MultiThreadProgramming
import ir.hrka.kotlin.presentation.ui.screens.about.AboutScreen
import ir.hrka.kotlin.presentation.ui.screens.point.PointsScreen
import ir.hrka.kotlin.presentation.ui.screens.topic.TopicsScreen
import ir.hrka.kotlin.presentation.ui.screens.home.HomeScreen
import ir.hrka.kotlin.presentation.ui.screens.splash.SplashScreen
import ir.hrka.kotlin.presentation.ui.screens.visualizers.multi_threading_programming.MultiThreadProgrammingScreen
import ir.hrka.kotlin.presentation.ui.screens.visualizers.sequential_programming.SequentialProgrammingScreen
import ir.hrka.kotlin.presentation.ui.theme.KotlinTheme

@Suppress("DEPRECATION")
@SuppressLint("NewApi")
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
                route = "${Topic()}/{$TOPICS_SCREEN_COURSE_ARGUMENT}",
                arguments = listOf(
                    navArgument(TOPICS_SCREEN_COURSE_ARGUMENT) {
                        type = NavType.EnumType(Course::class.java)
                    }
                )
            ) { backStackEntry ->
                val course = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    backStackEntry.arguments
                        ?.getParcelable(
                            TOPICS_SCREEN_COURSE_ARGUMENT,
                            Course::class.java
                        ) ?: Kotlin
                else
                    backStackEntry.arguments
                        ?.getParcelable<Course>(TOPICS_SCREEN_COURSE_ARGUMENT)
                        ?: Kotlin

                TopicsScreen(activity, navHostController, course)
            }
            composable(
                route = Point()
            ) {
                val topic = navHostController
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<ir.hrka.kotlin.domain.entities.db.Topic>(POINTS_SCREEN_TOPIC_ARGUMENT)

                PointsScreen(
                    activity,
                    navHostController,
                    topic
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
            composable(
                route = MultiThreadProgramming()
            ) {
                MultiThreadProgrammingScreen(activity, navHostController)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppContentPreview(modifier: Modifier = Modifier) {
    AppContent()
}
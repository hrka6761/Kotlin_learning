package ir.hrka.kotlin.presentation

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.hrka.kotlin.core.Constants.POINTS_SCREEN_TOPIC_ARGUMENT
import ir.hrka.kotlin.core.Constants.TOPICS_SCREEN_COURSE_ARGUMENT
import ir.hrka.kotlin.core.Constants.TOPICS_SCREEN_UPDATED_TOPIC_STATE_ID_ARGUMENT
import ir.hrka.kotlin.core.utilities.Screen.Splash
import ir.hrka.kotlin.core.utilities.Screen.Home
import ir.hrka.kotlin.core.utilities.Screen.Topic
import ir.hrka.kotlin.core.utilities.Screen.Point
import ir.hrka.kotlin.core.utilities.Screen.About
import ir.hrka.kotlin.core.utilities.Screen.SequentialProgramming
import ir.hrka.kotlin.core.utilities.Screen.MultiThreadProgramming
import ir.hrka.kotlin.core.utilities.Screen.Coroutines
import ir.hrka.kotlin.core.utilities.Screen.RunBlocking
import ir.hrka.kotlin.core.utilities.Screen.CoroutineScopeFunction
import ir.hrka.kotlin.core.utilities.Screen.RegularCoroutineScopeFunction
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.presentation.ui.screens.about.AboutScreen
import ir.hrka.kotlin.presentation.ui.screens.point.PointsScreen
import ir.hrka.kotlin.presentation.ui.screens.topic.TopicsScreen
import ir.hrka.kotlin.presentation.ui.screens.home.HomeScreen
import ir.hrka.kotlin.presentation.ui.screens.splash.SplashScreen
import ir.hrka.kotlin.presentation.ui.screens.visualizers.coroutine_scope_function.CoroutineScopeFunctionScreen
import ir.hrka.kotlin.presentation.ui.screens.visualizers.coroutines.CoroutinesScreen
import ir.hrka.kotlin.presentation.ui.screens.visualizers.multi_threading_programming.MultiThreadProgrammingScreen
import ir.hrka.kotlin.presentation.ui.screens.visualizers.run_blocking.RunBlockingScreen
import ir.hrka.kotlin.presentation.ui.screens.visualizers.sequential_programming.SequentialProgrammingScreen
import ir.hrka.kotlin.presentation.ui.theme.KotlinTheme

@Suppress("DEPRECATION")
@SuppressLint("NewApi")
@Composable
fun AppContent(modifier: Modifier = Modifier) {
    val navHostController = rememberNavController()
    val activity = (LocalContext.current as MainActivity)
    val snackBarHostState = remember { SnackbarHostState() }

    KotlinTheme {
        NavHost(
            modifier = modifier.fillMaxSize(),
            navController = navHostController,
            startDestination = Splash.destination
        ) {
            composable(
                route = Splash.destination
            ) {
                SplashScreen(
                    modifier = modifier,
                    activity = activity,
                    navHostController = navHostController,
                    snackBarHostState = snackBarHostState
                )
            }
            composable(
                route = Home.destination,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                HomeScreen(
                    modifier = modifier,
                    activity = activity,
                    navHostController = navHostController,
                    snackBarHostState = snackBarHostState
                )
            }
            composable(
                route = Topic.destination,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                val course = navHostController
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<Course>(TOPICS_SCREEN_COURSE_ARGUMENT)

                val updatedTopicId = navHostController
                    .currentBackStackEntry
                    ?.savedStateHandle
                    ?.get<Int>(TOPICS_SCREEN_UPDATED_TOPIC_STATE_ID_ARGUMENT)

                TopicsScreen(
                    modifier = modifier,
                    navHostController = navHostController,
                    snackBarHostState = snackBarHostState,
                    course = course,
                    updatedTopicId = updatedTopicId
                )
            }
            composable(
                route = Point.destination,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                val topic = navHostController
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<ir.hrka.kotlin.domain.entities.db.Topic>(POINTS_SCREEN_TOPIC_ARGUMENT)

                PointsScreen(
                    modifier = modifier,
                    activity = activity,
                    navHostController = navHostController,
                    snackBarHostState = snackBarHostState,
                    topic = topic
                )
            }
            composable(
                route = About.destination,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                AboutScreen(activity, navHostController)
            }
            composable(
                route = SequentialProgramming.destination,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                SequentialProgrammingScreen(activity, navHostController)
            }
            composable(
                route = MultiThreadProgramming.destination,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                MultiThreadProgrammingScreen(activity, navHostController)
            }
            composable(
                route = (Coroutines.destination),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                CoroutinesScreen(activity, navHostController)
            }
            composable(
                route = (RunBlocking.destination),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                RunBlockingScreen(activity, navHostController)
            }
            composable(
                route = (RegularCoroutineScopeFunction.destination),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                ir.hrka.kotlin.presentation.ui.screens.visualizers.regular_coroutine_scope_function.CoroutineScopeFunctionScreen(
                    activity,
                    navHostController
                )
            }
            composable(
                route = (CoroutineScopeFunction.destination),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                CoroutineScopeFunctionScreen(activity, navHostController)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppContentPreview(modifier: Modifier = Modifier) {
    AppContent()
}
package ir.hrka.kotlin.presentation

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.hrka.kotlin.core.Constants.BAZAAR_URL
import ir.hrka.kotlin.core.Constants.CLIPBOARD_LABEL
import ir.hrka.kotlin.core.Constants.CLIPBOARD_TEXT
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
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState.Stop
import ir.hrka.kotlin.core.utilities.sharePoint
import ir.hrka.kotlin.core.utilities.translatePoint
import ir.hrka.kotlin.domain.entities.Point
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.presentation.screens.about.AboutScreen
import ir.hrka.kotlin.presentation.screens.about.AboutViewModel
import ir.hrka.kotlin.presentation.screens.point.PointsScreen
import ir.hrka.kotlin.presentation.screens.topic.TopicsScreen
import ir.hrka.kotlin.presentation.screens.home.HomeScreen
import ir.hrka.kotlin.presentation.screens.home.HomeViewModel
import ir.hrka.kotlin.presentation.screens.point.PointsViewModel
import ir.hrka.kotlin.presentation.screens.splash.SplashScreen
import ir.hrka.kotlin.presentation.screens.splash.SplashViewModel
import ir.hrka.kotlin.presentation.screens.topic.TopicViewModel
import ir.hrka.kotlin.presentation.screens.visualizers.coroutine_scope_function.CoroutineScopeFunctionScreen
import ir.hrka.kotlin.presentation.screens.visualizers.coroutine_scope_function.CoroutineScopeFunctionViewModel
import ir.hrka.kotlin.presentation.screens.visualizers.coroutines.CoroutinesScreen
import ir.hrka.kotlin.presentation.screens.visualizers.coroutines.CoroutinesViewModel
import ir.hrka.kotlin.presentation.screens.visualizers.multi_threading_programming.MultiThreadProgrammingScreen
import ir.hrka.kotlin.presentation.screens.visualizers.multi_threading_programming.MultiThreadProgrammingViewModel
import ir.hrka.kotlin.presentation.screens.visualizers.regular_coroutine_scope_function.RegularCoroutineScopeFunctionScreen
import ir.hrka.kotlin.presentation.screens.visualizers.regular_coroutine_scope_function.RegularCoroutineScopeFunctionViewModel
import ir.hrka.kotlin.presentation.screens.visualizers.run_blocking.RunBlockingScreen
import ir.hrka.kotlin.presentation.screens.visualizers.run_blocking.RunBlockingViewModel
import ir.hrka.kotlin.presentation.screens.visualizers.sequential_programming.SequentialProgrammingScreen
import ir.hrka.kotlin.presentation.screens.visualizers.sequential_programming.SequentialProgrammingViewModel
import ir.hrka.kotlin.presentation.ui.theme.KotlinTheme

@Suppress("DEPRECATION")
@SuppressLint("NewApi", "SourceLockedOrientationActivity")
@Composable
fun AppContent(
    modifier: Modifier = Modifier,
    appVersionCode: Int
) {
    val navHostController = rememberNavController()
    val activity = (LocalContext.current as MainActivity)
    val onTopBarBackPressed: () -> Unit = {
        navHostController.popBackStack()
    }
    val blockRotation = {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
    val unblockRotation = {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    KotlinTheme {
        NavHost(
            modifier = modifier.fillMaxSize(),
            navController = navHostController,
            startDestination = Splash.destination
        ) {
            composable(
                route = Splash.destination
            ) {
                val splashViewModel: SplashViewModel = hiltViewModel()
                val executionState by splashViewModel.executionState.collectAsState()
                val updateState by splashViewModel.updateState.collectAsState()
                val updateDialogConfirmAction = {
                    val intent = Intent(Intent.ACTION_VIEW, BAZAAR_URL.toUri())
                    activity.startActivity(intent)
                }
                val updateDialogForceDismissAction = {
                    activity.finish()
                }
                val updateDialogOptionalDismissAction = {
                    navHostController.navigate(Home.destination)
                }
                val onCheckAction = {
                    splashViewModel.checkChangelog(appVersionCode)
                }
                val navigateToHome = {
                    navHostController.navigate(Home.destination) {
                        popUpTo(Splash.destination) { inclusive = true }
                    }
                }

                SplashScreen(
                    modifier = modifier,
                    updateDialogConfirmAction = updateDialogConfirmAction,
                    updateDialogForceDismissAction = updateDialogForceDismissAction,
                    updateDialogOptionalDismissAction = updateDialogOptionalDismissAction,
                    executionState = executionState,
                    updateState = updateState,
                    checkUpdate = onCheckAction,
                    navigateToHome = navigateToHome
                )
            }
            composable(
                route = Home.destination,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                val homeViewModel: HomeViewModel = hiltViewModel()
                val failedState by homeViewModel.failedState.collectAsState()
                val coursesResult by homeViewModel.courses.collectAsState()
                val executionState by homeViewModel.executionState.collectAsState()
                val navigateToAbout = {
                    navHostController.navigate(About.destination)
                }
                val fetchCourses = {
                    homeViewModel.getCourses()
                }
                val onClickCourseRow: (course: Course) -> Unit = { course ->
                    navHostController
                        .currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(TOPICS_SCREEN_COURSE_ARGUMENT, course)

                    navHostController.navigate(Topic.destination)
                }

                HomeScreen(
                    modifier = modifier,
                    navigateToAbout = navigateToAbout,
                    failedState = failedState,
                    coursesResult = coursesResult,
                    executionState = executionState,
                    fetchCourses = fetchCourses,
                    onClickCourseRow = onClickCourseRow
                )
            }
            composable(
                route = Topic.destination,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                val topicViewModel: TopicViewModel = hiltViewModel()
                val topics by topicViewModel.topics.collectAsState()
                val executionState by topicViewModel.executionState.collectAsState()
                val failedState by topicViewModel.failedState.collectAsState()
                val course = navHostController
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<Course>(TOPICS_SCREEN_COURSE_ARGUMENT)
                val updatedTopicId = navHostController
                    .currentBackStackEntry
                    ?.savedStateHandle
                    ?.get<Int>(TOPICS_SCREEN_UPDATED_TOPIC_STATE_ID_ARGUMENT)
                val updateTopicState = {
                    if (updatedTopicId != null)
                        topicViewModel.updateTopicStateInList(updatedTopicId)
                }
                val onClickTopicRow: (topic: Topic) -> Unit = { topic ->
                    if (topic.isActive) {
                        navHostController
                            .currentBackStackEntry
                            ?.savedStateHandle
                            ?.set(POINTS_SCREEN_TOPIC_ARGUMENT, topic)

                        navHostController.navigate(Point.destination)
                    }
                }
                val fetchTopics = {
                    topicViewModel.getTopics(course)
                }

                TopicsScreen(
                    modifier = modifier,
                    course = course,
                    onTopBarBackPressed = onTopBarBackPressed,
                    topicsResult = topics,
                    executionState = executionState,
                    failedState = failedState,
                    updateTopicState = updateTopicState,
                    onClickTopicRow = onClickTopicRow,
                    fetchTopics = fetchTopics,
                    appVersionCode = topicViewModel.appVersionCode
                )
            }
            composable(
                route = Point.destination,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                val pointViewModel: PointsViewModel = hiltViewModel()
                val points by pointViewModel.points.collectAsState()
                val failedState by pointViewModel.failedState.collectAsState()
                val executionState by pointViewModel.executionState.collectAsState()
                val updateTopicStateOnDBResult by pointViewModel.updateTopicStateOnDBResult.collectAsState()
                val topic = navHostController
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<Topic>(POINTS_SCREEN_TOPIC_ARGUMENT)
                val navigateToVisualizer = {
                    if (topic?.visualizerDestination?.isNotEmpty() == true)
                        navHostController.navigate(topic.visualizerDestination)
                }
                val fetchPoints = {
                    pointViewModel.getPoints(topic)
                }
                val onSharePoint: (point: Point) -> Unit = { point ->
                    activity.sharePoint(point)
                }
                val onTranslatePoint: (point: Point) -> Unit = { point ->
                    activity.translatePoint(point)
                }
                val setUpdatedTopicId: () -> Unit = {
                    navHostController
                        .previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(TOPICS_SCREEN_UPDATED_TOPIC_STATE_ID_ARGUMENT, topic?.id)
                }

                PointsScreen(
                    modifier = modifier,
                    topic = topic,
                    onTopBarBackPressed = onTopBarBackPressed,
                    appVersionCode = pointViewModel.appVersionCode,
                    navigateToVisualizer = navigateToVisualizer,
                    pointsResult = points,
                    updateTopicStateOnDBResult = updateTopicStateOnDBResult,
                    setUpdatedTopicId = setUpdatedTopicId,
                    executionState = executionState,
                    failedState = failedState,
                    fetchPoints = fetchPoints,
                    onSharePoint = onSharePoint,
                    onTranslatePoint = onTranslatePoint
                )
            }
            composable(
                route = About.destination,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                val aboutViewModel: AboutViewModel = hiltViewModel()
                val onClickEmail = {
                    val clipboard =
                        activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText(CLIPBOARD_LABEL, CLIPBOARD_TEXT)
                    clipboard.setPrimaryClip(clip)
                }

                AboutScreen(
                    modifier = modifier,
                    onTopBarBackPressed = onTopBarBackPressed,
                    onClickEmail = onClickEmail,
                    openUrl = { url ->
                        openUrl(
                            context = activity,
                            url = url
                        )
                    }
                )
            }
            composable(
                route = SequentialProgramming.destination,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                val viewModel: SequentialProgrammingViewModel = hiltViewModel()
                val executionState by viewModel.executionState.collectAsState()
                val mainThreadState by viewModel.mainThreadState.observeAsState(initial = Stop())
                val task1State by viewModel.task1State.observeAsState(initial = Stop())
                val task2State by viewModel.task2State.observeAsState(initial = Stop())
                val task3State by viewModel.task3State.observeAsState(initial = Stop())
                val runVisualizer = {
                    viewModel.runAllTasks()
                }
                val restartVisualizer = {
                    viewModel.restartVisualizer()
                }

                SequentialProgrammingScreen(
                    modifier = modifier,
                    onTopBarBackPressed = onTopBarBackPressed,
                    executionState = executionState,
                    blockRotation = blockRotation,
                    unblockRotation = unblockRotation,
                    runVisualizer = runVisualizer,
                    restartVisualizer = restartVisualizer,
                    mainThreadState = mainThreadState,
                    task1State = task1State,
                    task2State = task2State,
                    task3State = task3State
                )
            }
            composable(
                route = MultiThreadProgramming.destination,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                val viewModel: MultiThreadProgrammingViewModel = hiltViewModel()
                val executionState by viewModel.executionState.collectAsState()
                val mainThreadState by viewModel.mainThreadState.observeAsState(initial = Stop())
                val thread1State by viewModel.thread1State.observeAsState(initial = Stop())
                val thread2State by viewModel.thread2State.observeAsState(initial = Stop())
                val task1State by viewModel.task1State.observeAsState(initial = Stop())
                val task2State by viewModel.task2State.observeAsState(initial = Stop())
                val task3State by viewModel.task3State.observeAsState(initial = Stop())
                val task4State by viewModel.task4State.observeAsState(initial = Stop())
                val task5State by viewModel.task5State.observeAsState(initial = Stop())
                val task6State by viewModel.task6State.observeAsState(initial = Stop())
                val runVisualizer = {
                    viewModel.runAllTasks()
                }
                val restartVisualizer = {
                    viewModel.restartVisualizer()
                }

                MultiThreadProgrammingScreen(
                    modifier = modifier,
                    onTopBarBackPressed = onTopBarBackPressed,
                    executionState = executionState,
                    blockRotation = blockRotation,
                    unblockRotation = unblockRotation,
                    runVisualizer = runVisualizer,
                    restartVisualizer = restartVisualizer,
                    mainThreadState = mainThreadState,
                    thread1State = thread1State,
                    thread2State = thread2State,
                    task1State = task1State,
                    task2State = task2State,
                    task3State = task3State,
                    task4State = task4State,
                    task5State = task5State,
                    task6State = task6State
                )
            }
            composable(
                route = (Coroutines.destination),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                val viewModel: CoroutinesViewModel = hiltViewModel()
                val executionState by viewModel.executionState.collectAsState()
                val mainThreadState by viewModel.mainThreadState.observeAsState(initial = Stop())
                val coroutine1State by viewModel.coroutine1State.observeAsState(initial = Stop())
                val coroutine2State by viewModel.coroutine2State.observeAsState(initial = Stop())
                val task1State by viewModel.task1State.observeAsState(initial = Stop())
                val task2State by viewModel.task2State.observeAsState(initial = Stop())
                val task3State by viewModel.task3State.observeAsState(initial = Stop())
                val task4State by viewModel.task4State.observeAsState(initial = Stop())
                val task5State by viewModel.task5State.observeAsState(initial = Stop())
                val task6State by viewModel.task6State.observeAsState(initial = Stop())
                val runVisualizer = {
                    viewModel.runAllTasks()
                }
                val restartVisualizer = {
                    viewModel.restartVisualizer()
                }

                CoroutinesScreen(
                    modifier = modifier,
                    onTopBarBackPressed = onTopBarBackPressed,
                    executionState = executionState,
                    blockRotation = blockRotation,
                    unblockRotation = unblockRotation,
                    runVisualizer = runVisualizer,
                    restartVisualizer = restartVisualizer,
                    mainThreadState = mainThreadState,
                    coroutine1State = coroutine1State,
                    coroutine2State = coroutine2State,
                    task1State = task1State,
                    task2State = task2State,
                    task3State = task3State,
                    task4State = task4State,
                    task5State = task5State,
                    task6State = task6State
                )
            }
            composable(
                route = (RunBlocking.destination),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                val viewModel: RunBlockingViewModel = hiltViewModel()
                val executionState by viewModel.executionState.collectAsState()
                val mainThreadState by viewModel.mainThreadState.observeAsState(initial = Stop())
                val coroutine1State by viewModel.coroutine1State.observeAsState(initial = Stop())
                val coroutine2State by viewModel.coroutine2State.observeAsState(initial = Stop())
                val coroutine3State by viewModel.coroutine3State.observeAsState(initial = Stop())
                val task1State by viewModel.task1State.observeAsState(initial = Stop())
                val task2State by viewModel.task2State.observeAsState(initial = Stop())
                val task3State by viewModel.task3State.observeAsState(initial = Stop())
                val task4State by viewModel.task4State.observeAsState(initial = Stop())
                val runVisualizer = {
                    viewModel.runAllTasks()
                }
                val restartVisualizer = {
                    viewModel.restartVisualizer()
                }

                RunBlockingScreen(
                    modifier = modifier,
                    onTopBarBackPressed = onTopBarBackPressed,
                    executionState = executionState,
                    blockRotation = blockRotation,
                    unblockRotation = unblockRotation,
                    runVisualizer = runVisualizer,
                    restartVisualizer = restartVisualizer,
                    mainThreadState = mainThreadState,
                    coroutine1State = coroutine1State,
                    coroutine2State = coroutine2State,
                    coroutine3State = coroutine3State,
                    task1State = task1State,
                    task2State = task2State,
                    task3State = task3State,
                    task4State = task4State
                )
            }
            composable(
                route = (RegularCoroutineScopeFunction.destination),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                val viewModel: RegularCoroutineScopeFunctionViewModel = hiltViewModel()
                val executionState by viewModel.executionState.collectAsState()
                val mainThreadState by viewModel.mainThreadState.observeAsState(initial = Stop())
                val scopeState by viewModel.scopeState.observeAsState(initial = Stop())
                val coroutine1State by viewModel.coroutine1State.observeAsState(initial = Stop())
                val coroutine2State by viewModel.coroutine2State.observeAsState(initial = Stop())
                val coroutine3State by viewModel.coroutine3State.observeAsState(initial = Stop())
                val task1State by viewModel.task1State.observeAsState(initial = Stop())
                val task2State by viewModel.task2State.observeAsState(initial = Stop())
                val task3State by viewModel.task3State.observeAsState(initial = Stop())
                val runVisualizer = {
                    viewModel.runAllTasks()
                }
                val restartVisualizer = {
                    viewModel.restartVisualizer()
                }

                RegularCoroutineScopeFunctionScreen(
                    modifier = modifier,
                    onTopBarBackPressed = onTopBarBackPressed,
                    executionState = executionState,
                    blockRotation = blockRotation,
                    unblockRotation = unblockRotation,
                    runVisualizer = runVisualizer,
                    restartVisualizer = restartVisualizer,
                    mainThreadState = mainThreadState,
                    scopeState = scopeState,
                    coroutine1State = coroutine1State,
                    coroutine2State = coroutine2State,
                    coroutine3State = coroutine3State,
                    task1State = task1State,
                    task2State = task2State,
                    task3State = task3State
                )
            }
            composable(
                route = (CoroutineScopeFunction.destination),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                val viewModel: CoroutineScopeFunctionViewModel = hiltViewModel()
                val executionState by viewModel.executionState.collectAsState()
                val mainThreadState by viewModel.mainThreadState.observeAsState(initial = Stop())
                val scopeState by viewModel.scopeState.observeAsState(initial = Stop())
                val coroutine1State by viewModel.coroutine1State.observeAsState(initial = Stop())
                val coroutine2State by viewModel.coroutine2State.observeAsState(initial = Stop())
                val coroutine3State by viewModel.coroutine3State.observeAsState(initial = Stop())
                val task1State by viewModel.task1State.observeAsState(initial = Stop())
                val task2State by viewModel.task2State.observeAsState(initial = Stop())
                val task3State by viewModel.task3State.observeAsState(initial = Stop())
                val task4State by viewModel.task4State.observeAsState(initial = Stop())
                val task5State by viewModel.task5State.observeAsState(initial = Stop())
                val task6State by viewModel.task6State.observeAsState(initial = Stop())
                val runVisualizer = {
                    viewModel.runAllTasks()
                }
                val restartVisualizer = {
                    viewModel.restartVisualizer()
                }

                CoroutineScopeFunctionScreen(
                    modifier = modifier,
                    onTopBarBackPressed = onTopBarBackPressed,
                    executionState = executionState,
                    blockRotation = blockRotation,
                    unblockRotation = unblockRotation,
                    runVisualizer = runVisualizer,
                    restartVisualizer = restartVisualizer,
                    mainThreadState = mainThreadState,
                    scopeState = scopeState,
                    coroutine1State = coroutine1State,
                    coroutine2State = coroutine2State,
                    coroutine3State = coroutine3State,
                    task1State = task1State,
                    task2State = task2State,
                    task3State = task3State,
                    task4State = task4State,
                    task5State = task5State,
                    task6State = task6State,
                )
            }
        }
    }
}

private val customTabsIntent =
    CustomTabsIntent.Builder()
        .setShowTitle(true)
        .setUrlBarHidingEnabled(true)
        .build()

private fun openUrl(context: Context, url: String) =
    customTabsIntent.launchUrl(context, url.toUri())


@Preview(showBackground = true)
@Composable
fun AppContentPreview(modifier: Modifier = Modifier) {
    AppContent(appVersionCode = 0)
}
package ir.hrka.kotlin.presentation.ui.screens.visualizers.regular_coroutine_scope_function

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ir.hrka.kotlin.R
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState.Stop
import ir.hrka.kotlin.presentation.MainActivity
import ir.hrka.kotlin.presentation.ui.screens.visualizers.Coroutine
import ir.hrka.kotlin.presentation.ui.screens.visualizers.Guidance
import ir.hrka.kotlin.presentation.ui.screens.visualizers.Scope
import ir.hrka.kotlin.presentation.ui.screens.visualizers.Task
import ir.hrka.kotlin.presentation.ui.screens.visualizers.Thread

@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun CoroutineScopeFunctionScreen(activity: MainActivity, navHostController: NavHostController) {

    val viewModel: CoroutineScopeFunctionViewModel = hiltViewModel()
    val snackBarHostState = remember { SnackbarHostState() }
    val executionState by viewModel.executionState.collectAsState()
    val mainThreadState by viewModel.mainThreadState.observeAsState(initial = Stop())
    val scopeState by viewModel.scopeState.observeAsState(initial = Stop())
    val coroutine1State by viewModel.coroutine1State.observeAsState(initial = Stop())
    val coroutine2State by viewModel.coroutine2State.observeAsState(initial = Stop())
    val coroutine3State by viewModel.coroutine3State.observeAsState(initial = Stop())
    val task1State by viewModel.task1State.observeAsState(initial = Stop())
    val task2State by viewModel.task2State.observeAsState(initial = Stop())
    val task3State by viewModel.task3State.observeAsState(initial = Stop())
    val snippetCodeState = remember { mutableStateOf(false) }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RegularCoroutineScopeFunctionScreenAppBar(
                navHostController,
                viewModel,
                executionState,
                snippetCodeState
            )
        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .fillMaxWidth(),
                hostState = snackBarHostState
            )
        }
    ) { innerPaddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (snippetCodeState.value)
                AlertDialog(
                    modifier = Modifier.fillMaxWidth(),
                    onDismissRequest = { snippetCodeState.value = false },
                    confirmButton = {
                        TextButton(
                            onClick = { snippetCodeState.value = false }
                        ) {
                            Text(
                                text = "Close",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    dismissButton = {},
                    title = {
                        Text(
                            text = "Visualizer Code",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                text = "Running three tasks by coroutines in the specific scope in the same context.\n"
                            )
                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    text = "fun main() {\n" +
                                            "    val scope = CoroutineScope(...)\n" +
                                            "    \n" +
                                            "    scope.launch {\n" +
                                            "        task1()\n" +
                                            "    }\n" +
                                            "    \n" +
                                            "    scope.launch {\n" +
                                            "        task2()\n" +
                                            "    }\n" +
                                            "\n" +
                                            "    scope.launch {\n" +
                                            "        task3()\n" +
                                            "    }\n" +
                                            "}\n" +
                                            "\n" +
                                            "\n" +
                                            "private suspend fun task1() {\n" +
                                            "    delay(4_000)\n" +
                                            "}\n" +
                                            "\n" +
                                            "private suspend fun task2() {\n" +
                                            "    delay(2_000)\n" +
                                            "}\n" +
                                            "\n" +
                                            "private suspend fun task3() {\n" +
                                            "    delay(3_000)\n" +
                                            "}"
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                )
            Guidance()
            Thread(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                state = mainThreadState
            ) {
                Scope(
                    state = scopeState
                ) {
                    Coroutine(
                        state = coroutine1State
                    ) {
                        Task(state = task1State)
                    }

                    Coroutine(
                        state = coroutine2State
                    ) {
                        Task(state = task2State)
                    }

                    Coroutine(
                        state = coroutine3State
                    ) {
                        Task(state = task3State)
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    LaunchedEffect(Unit) {
        if (executionState == ExecutionState.Start) {
            viewModel.setExecutionState(ExecutionState.Loading)
            viewModel.runAllTasks()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegularCoroutineScopeFunctionScreenAppBar(
    navHostController: NavHostController,
    viewModel: CoroutineScopeFunctionViewModel,
    executionState: ExecutionState,
    snippetCodeState: MutableState<Boolean>
) {
    TopAppBar(
        title = {
            Text(
                modifier = Modifier.padding(end = 16.dp),
                text = stringResource(R.string.regular_coroutine_scope_function_app_bar_title),
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { navHostController.popBackStack() }
            ) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            Icon(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable { snippetCodeState.value = true },
                painter = painterResource(R.drawable.snippet_codes),
                contentDescription = null
            )

            Icon(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .alpha(if (executionState == ExecutionState.Stop) 1f else 0.2f)
                    .clickable(enabled = executionState == ExecutionState.Stop) {
                        viewModel.restartVisualizer()
                    },
                painter = painterResource(R.drawable.restart_visualizer),
                contentDescription = null
            )
        }
    )
}
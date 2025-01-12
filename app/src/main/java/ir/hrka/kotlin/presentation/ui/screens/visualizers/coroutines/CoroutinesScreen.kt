package ir.hrka.kotlin.presentation.ui.screens.visualizers.coroutines

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ir.hrka.kotlin.presentation.MainActivity
import ir.hrka.kotlin.R
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState.Stop
import ir.hrka.kotlin.presentation.ui.screens.visualizers.Coroutine
import ir.hrka.kotlin.presentation.ui.screens.visualizers.Guidance
import ir.hrka.kotlin.presentation.ui.screens.visualizers.Task
import ir.hrka.kotlin.presentation.ui.screens.visualizers.Thread

@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun CoroutinesScreen(activity: MainActivity, navHostController: NavHostController) {

    val viewModel: CoroutinesViewModel = hiltViewModel()
    val snackBarHostState = remember { SnackbarHostState() }
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


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { CoroutinesScreenAppBar(navHostController, viewModel, executionState) },
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
            Guidance()
            Thread(
                modifier = Modifier.fillMaxSize(),
                state = mainThreadState
            ) {
                Task(state = task1State)

                Coroutine(
                    state = coroutine1State
                ) {
                    Task(state = task2State)
                    Task(state = task3State)
                }

                Coroutine(
                    state = coroutine2State
                ) {
                    Task(state = task4State)
                    Task(state = task5State)
                }

                Task(state = task6State)
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
fun CoroutinesScreenAppBar(
    navHostController: NavHostController,
    viewModel: CoroutinesViewModel,
    executionState: ExecutionState
) {
    TopAppBar(
        title = {
            Text(
                modifier = Modifier.padding(end = 16.dp),
                text = stringResource(R.string.coroutines_app_bar_title),
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
                    .clickable(enabled = executionState == ExecutionState.Stop) {
                        viewModel.restartVisualizer()
                    },
                painter = painterResource(R.drawable.restart_visualizer),
                contentDescription = null
            )
        }
    )
}


@Preview(showBackground = true)
@Composable
fun CoroutinesScreenPreview() {
    CoroutinesScreen(MainActivity(), rememberNavController())
}

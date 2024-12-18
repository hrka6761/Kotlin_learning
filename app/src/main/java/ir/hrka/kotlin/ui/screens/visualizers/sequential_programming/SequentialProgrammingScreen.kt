package ir.hrka.kotlin.ui.screens.visualizers.sequential_programming

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ir.hrka.kotlin.MainActivity
import ir.hrka.kotlin.R
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.CoroutineComponentState.Stop
import ir.hrka.kotlin.ui.screens.visualizers.Guidance
import ir.hrka.kotlin.ui.screens.visualizers.Task
import ir.hrka.kotlin.ui.screens.visualizers.Thread

@Composable
fun SequentialProgrammingScreen(activity: MainActivity, navHostController: NavHostController) {

    val viewModel: SequentialProgrammingViewModel = hiltViewModel()
    val snackBarHostState = remember { SnackbarHostState() }
    val executionState by viewModel.executionState.collectAsState()
    val mainThreadState by viewModel.mainThreadState.observeAsState(initial = Stop())
    val task1State by viewModel.task1State.observeAsState(initial = Stop())
    val task2State by viewModel.task2State.observeAsState(initial = Stop())
    val task3State by viewModel.task3State.observeAsState(initial = Stop())


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { SequentialProgrammingAppBar(navHostController) },
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
                Task(state = task2State)
                Task(state = task3State)
            }
        }
    }

    LaunchedEffect(Unit) {
        if (executionState == ExecutionState.Start) {
            viewModel.setExecutionState(ExecutionState.Loading)
            viewModel.runAllTasks()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SequentialProgrammingAppBar(navHostController: NavHostController) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.sequential_programming_app_bar_title)) },
        navigationIcon = {
            IconButton(
                onClick = { navHostController.popBackStack() }
            ) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun SequentialProgrammingScreenPreview() {
    SequentialProgrammingScreen(MainActivity(), rememberNavController())
}

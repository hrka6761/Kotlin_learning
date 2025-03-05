package ir.hrka.kotlin.presentation.ui.screens.visualizers.coroutine_context

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
import androidx.compose.ui.tooling.preview.Preview
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
fun CoroutineContextScreen(activity: MainActivity, navHostController: NavHostController) {

    val viewModel: CoroutineContextViewModel = hiltViewModel()
    val snackBarHostState = remember { SnackbarHostState() }
    val snippetCodeState = remember { mutableStateOf(false) }
    val executionState by viewModel.executionState.collectAsState()
    val mainThreadState by viewModel.mainThreadState.observeAsState(initial = Stop())
    val coroutine1State by viewModel.coroutine1State.observeAsState(initial = Stop())
    val coroutine2State by viewModel.coroutine2State.observeAsState(initial = Stop())
    val coroutine3State by viewModel.coroutine3State.observeAsState(initial = Stop())
    val task1State by viewModel.task1State.observeAsState(initial = Stop())
    val task2State by viewModel.task2State.observeAsState(initial = Stop())


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CoroutineScopeFunctionScreenAppBar(
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
                                text = "Three coroutines are launched in one scope and three different contexts."
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
                                    text = " fun main() {\n" +
                                            "     val scopeJob = Job()\n" +
                                            "     val threads = Dispatchers.Main\n" +
                                            "     val coroutineName = CoroutineName(\"outer_coroutine\")\n" +
                                            "     val innerCoroutineJob1 = Job()\n" +
                                            "     val innerCoroutineThreads1 = Dispatchers.IO\n" +
                                            "     val innerCoroutineName1 = CoroutineName(\"first_inner_coroutine\")\n" +
                                            "     val innerCoroutineJob2 = Job()\n" +
                                            "     val innerCoroutineThreads2 = Dispatchers.Default\n" +
                                            "     val innerCoroutineName2 = CoroutineName(\"second_inner_coroutine\")\n\n" +
                                            "     CoroutineScope(\n" +
                                            "         scopeJob\n" +
                                            "                 + threads\n" +
                                            "                 + coroutineName\n" +
                                            "     ).launch {\n" +
                                            "         launch(\n" +
                                            "             innerCoroutineJob1\n" +
                                            "                     + innerCoroutineThreads1\n" +
                                            "                     + innerCoroutineName1\n" +
                                            "         ) {\n" +
                                            "             task1()\n" +
                                            "         }\n" +
                                            "         launch(innerCoroutineJob2\n" +
                                            "                 + innerCoroutineThreads2\n" +
                                            "                 + innerCoroutineName2\n" +
                                            "         ) {\n" +
                                            "             task2()\n" +
                                            "         }\n" +
                                            "     }\n" +
                                            " }"
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
                Coroutine(
                    state = coroutine1State
                ) {
                    Coroutine(
                        state = coroutine2State
                    ) {
                        Task(
                            state = task1State
                        )
                    }

                    Coroutine(
                        state = coroutine3State
                    ) {
                        Task(
                            state = task2State
                        )
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
fun CoroutineScopeFunctionScreenAppBar(
    navHostController: NavHostController,
    viewModel: CoroutineContextViewModel,
    executionState: ExecutionState,
    snippetCodeState: MutableState<Boolean>
) {
    TopAppBar(
        title = {
            Text(
                modifier = Modifier.padding(end = 16.dp),
                text = stringResource(R.string.coroutine_context_app_bar_title),
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


@Preview(showBackground = true)
@Composable
fun CoroutineContextScreenPreview(modifier: Modifier = Modifier) {

}
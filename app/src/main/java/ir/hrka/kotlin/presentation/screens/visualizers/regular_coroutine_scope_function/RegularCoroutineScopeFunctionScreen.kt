package ir.hrka.kotlin.presentation.screens.visualizers.regular_coroutine_scope_function

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.hrka.kotlin.R
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.CoroutineData
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ScopeData
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.TaskData
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ThreadData
import ir.hrka.kotlin.presentation.TopBar
import ir.hrka.kotlin.presentation.screens.visualizers.Coroutine
import ir.hrka.kotlin.presentation.screens.visualizers.Guidance
import ir.hrka.kotlin.presentation.screens.visualizers.Scope
import ir.hrka.kotlin.presentation.screens.visualizers.Task
import ir.hrka.kotlin.presentation.screens.visualizers.Thread
import ir.hrka.kotlin.presentation.screens.visualizers.VisualizerSnippetCodeDialog
import kotlinx.coroutines.Job

@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun RegularCoroutineScopeFunctionScreen(
    modifier: Modifier = Modifier,
    onTopBarBackPressed: () -> Unit,
    executionState: ExecutionState,
    blockRotation: () -> Unit,
    unblockRotation: () -> Unit,
    runVisualizer: () -> Unit,
    restartVisualizer: () -> Unit,
    mainThreadState: ComponentState<ThreadData>,
    scopeState: ComponentState<ScopeData>,
    coroutine1State: ComponentState<CoroutineData>,
    coroutine2State: ComponentState<CoroutineData>,
    coroutine3State: ComponentState<CoroutineData>,
    task1State: ComponentState<TaskData>,
    task2State: ComponentState<TaskData>,
    task3State: ComponentState<TaskData>
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val snippetCodeState = remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                modifier = modifier,
                title = stringResource(R.string.regular_coroutine_scope_function_app_bar_title),
                navigationClick = onTopBarBackPressed,
                actions = {
                    Icon(
                        modifier = modifier
                            .padding(end = 8.dp)
                            .clickable { snippetCodeState.value = true },
                        painter = painterResource(R.drawable.snippet_codes),
                        contentDescription = null
                    )

                    Icon(
                        modifier = modifier
                            .padding(end = 8.dp)
                            .alpha(if (executionState == ExecutionState.Stop) 1f else 0.2f)
                            .clickable(enabled = executionState == ExecutionState.Stop) {
                                restartVisualizer()
                            },
                        painter = painterResource(R.drawable.restart_visualizer),
                        contentDescription = null
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                modifier = modifier.fillMaxWidth(),
                hostState = snackBarHostState
            )
        }
    ) { innerPaddings ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPaddings),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

            VisualizerSnippetCodeDialog(
                modifier = modifier,
                snippetCodeState = snippetCodeState,
                description = stringResource(R.string.regular_coroutine_scope_function_screen_snippet_code_dialog_desc),
                snippetCode = stringResource(R.string.regular_coroutine_scope_function_screen_snippet_code_dialog_code)
            )
        }
    }

    DisposableEffect(Unit) {
        blockRotation()
        runVisualizer()
        onDispose {
            unblockRotation()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegularCoroutineScopeFunctionScreenPreview() {
    RegularCoroutineScopeFunctionScreen(
        onTopBarBackPressed = {},
        executionState = ExecutionState.Stop,
        blockRotation = {},
        unblockRotation = {},
        runVisualizer = {},
        restartVisualizer = {},
        mainThreadState = ComponentState.Processing(
            ThreadData(
                threadId = "2",
                threadName = "main"
            )
        ),
        scopeState = ComponentState.Processing(
            ScopeData(
                scopeName = "CoroutineScopeFunction",
                coroutineName = "@coroutine#742",
                thread = "main",
                job = Job(),
                parentJob = Job(),
                children = listOf(Job(), Job())
            )
        ),
        coroutine1State = ComponentState.Processing(
            CoroutineData(
                coroutineName = "@coroutine#742",
                thread = "main",
                job = Job(),
                parentJob = Job(),
                children = listOf(Job(), Job())
            )
        ),
        coroutine2State = ComponentState.Processing(
            CoroutineData(
                coroutineName = "@coroutine#743",
                thread = "main",
                job = Job(),
                parentJob = Job(),
                children = listOf(Job(), Job())
            )
        ),
        coroutine3State = ComponentState.Processing(
            CoroutineData(
                coroutineName = "@coroutine#743",
                thread = "main",
                job = Job(),
                parentJob = Job(),
                children = listOf(Job(), Job())
            )
        ),
        task1State = ComponentState.Processing(
            TaskData(
                taskName = "Task 1",
                durationTime = "1000"
            )
        ),
        task2State = ComponentState.Processing(
            TaskData(
                taskName = "Task 2",
                durationTime = "1000"
            )
        ),
        task3State = ComponentState.Processing(
            TaskData(
                taskName = "Task 3",
                durationTime = "1000"
            )
        )
    )
}
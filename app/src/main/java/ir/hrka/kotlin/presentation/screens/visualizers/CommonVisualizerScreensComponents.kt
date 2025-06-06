package ir.hrka.kotlin.presentation.screens.visualizers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.hrka.kotlin.R
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentData
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState.Stop
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState.Processing
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState.Done
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState.Cancel
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ComponentState.Failed
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.CoroutineData
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ScopeData
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.TaskData
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ThreadData
import kotlinx.coroutines.Job

@Composable
fun Thread(
    modifier: Modifier = Modifier,
    state: ComponentState<ThreadData>,
    content: @Composable () -> Unit
) {
    val threadData = state.componentData

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            val styledText = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.thread_name_title))
                }
                append("${threadData?.threadName ?: "..."}\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.thread_id_title))
                }
                append(threadData?.threadId ?: "...")
            }

            Text(
                modifier = Modifier.padding(start = 8.dp, end = 16.dp),
                text = styledText,
                style = TextStyle(
                    fontSize = 10.sp,
                    lineHeight = 12.sp
                ),
            )

            StateIcon(data = state)
        }
        content()
    }
}

@Composable
fun Coroutine(
    modifier: Modifier = Modifier,
    state: ComponentState<CoroutineData>,
    content: @Composable () -> Unit
) {
    val coroutineData = state.componentData

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            val styledText = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.coroutine_name_title))
                }
                append("${coroutineData?.coroutineName ?: "..."}\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.context_thread_title))
                }
                append("${coroutineData?.thread ?: "..."}\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.context_job_title))
                }
                append("${coroutineData?.job ?: "..."}\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.parent_job_title))
                }
                append("${coroutineData?.parentJob ?: "..."}\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.children_title))
                }

                if (coroutineData?.children.isNullOrEmpty())
                    append(stringResource(R.string.no_child))

                coroutineData?.children?.forEachIndexed { index, job ->
                    val id = index + 1
                    append("$id) ${job}\n")
                }
            }

            Text(
                modifier = modifier
                    .weight(1f, fill = false)
                    .padding(start = 8.dp, end = 16.dp),
                text = styledText,
                style = TextStyle(
                    fontSize = 10.sp,
                    lineHeight = 12.sp
                )
            )

            StateIcon(data = state)
        }
        content()
    }
}

@Composable
fun Task(
    modifier: Modifier = Modifier,
    state: ComponentState<TaskData>
) {
    val taskData = state.componentData

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            val styledText = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.task_name_title))
                }
                append("${taskData?.taskName ?: "..."}\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.duration_time_title))
                }
                append("${taskData?.durationTime ?: "..."} MS")
            }

            Text(
                modifier = modifier.padding(start = 8.dp, end = 16.dp),
                text = styledText,
                style = TextStyle(
                    fontSize = 10.sp,
                    lineHeight = 12.sp
                )
            )

            StateIcon(data = state)
        }
    }
}

@Composable
fun Scope(
    modifier: Modifier = Modifier,
    state: ComponentState<ScopeData>,
    content: @Composable () -> Unit
) {
    val coroutineData = state.componentData

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = RoundedCornerShape(4)
            ),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            val styledText = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.scope_name_title))
                }
                append("${coroutineData?.scopeName ?: "..."}\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.coroutine_name_title))
                }
                append("${coroutineData?.coroutineName ?: "..."}\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.context_thread_title))
                }
                append("${coroutineData?.thread ?: "..."}\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.context_job_title))
                }
                append("${coroutineData?.job ?: "..."}\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.parent_job_title))
                }
                append("${coroutineData?.parentJob ?: "..."}\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.children_title))
                }

                if (coroutineData?.children.isNullOrEmpty())
                    append(stringResource(R.string.no_child))

                coroutineData?.children?.forEachIndexed { index, job ->
                    val id = index + 1
                    append("$id) ${job}\n")
                }
            }

            Text(
                modifier = modifier
                    .weight(1f, fill = false)
                    .padding(start = 8.dp, end = 16.dp),
                text = styledText,
                style = TextStyle(
                    fontSize = 10.sp,
                    lineHeight = 12.sp
                )
            )

            StateIcon(data = state)
        }

        content()
    }
}

@Composable
fun StateIcon(
    modifier: Modifier = Modifier.size(25.dp),
    data: ComponentState<ComponentData>,
) {
    when (data) {
        is Stop -> {
            Icon(
                modifier = modifier,
                painter = painterResource(R.drawable.state_stop),
                contentDescription = null
            )
        }

        is Processing -> {
            CircularProgressIndicator(
                modifier = modifier,
                strokeWidth = 1.dp
            )
        }

        is Done -> {
            Icon(
                modifier = modifier,
                painter = painterResource(R.drawable.state_done),
                contentDescription = null
            )
        }

        is Cancel -> {
            Icon(
                modifier = modifier,
                painter = painterResource(R.drawable.state_cancel),
                contentDescription = null
            )
        }

        is Failed -> {
            Icon(
                modifier = modifier,
                painter = painterResource(R.drawable.state_failed),
                contentDescription = null
            )
        }
    }
}

@Composable
fun Guidance(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = modifier
                    .size(15.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            )

            Text(
                modifier = modifier.padding(start = 2.dp, end = 10.dp),
                fontSize = 10.sp,
                text = stringResource(R.string.guidance_thread)
            )

            Box(
                modifier = modifier
                    .size(15.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            )

            Text(
                modifier = modifier.padding(start = 2.dp, end = 10.dp),
                fontSize = 10.sp,
                text = stringResource(R.string.guidance_coroutine)
            )

            Box(
                modifier = modifier
                    .size(15.dp)
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
            )

            Text(
                modifier = modifier.padding(start = 2.dp, end = 10.dp),
                fontSize = 10.sp,
                text = stringResource(R.string.guidance_task)
            )

            ElevatedCard(
                modifier = modifier
                    .size(15.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        shape = RoundedCornerShape(20)
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {}

            Text(
                modifier = modifier.padding(start = 2.dp, end = 10.dp),
                fontSize = 10.sp,
                text = stringResource(R.string.guidance_scope)
            )
        }

        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.state_stop),
                contentDescription = null
            )

            Text(
                modifier = modifier.padding(start = 2.dp, end = 8.dp),
                fontSize = 10.sp,
                text = stringResource(R.string.guidance_stop)
            )

            CircularProgressIndicator(
                modifier = modifier
                    .width(15.dp)
                    .height(15.dp)
                    .padding(end = 2.dp),
                strokeWidth = 1.dp
            )

            Text(
                modifier = modifier.padding(start = 2.dp, end = 8.dp),
                fontSize = 10.sp,
                text = stringResource(R.string.guidance_processing)
            )

            Icon(
                painter = painterResource(R.drawable.state_done),
                contentDescription = null
            )

            Text(
                modifier = modifier.padding(start = 2.dp, end = 8.dp),
                fontSize = 10.sp,
                text = stringResource(R.string.guidance_done)
            )

            Icon(
                painter = painterResource(R.drawable.state_cancel),
                contentDescription = null
            )

            Text(
                modifier = modifier.padding(start = 2.dp, end = 8.dp),
                fontSize = 10.sp,
                text = stringResource(R.string.guidance_cancel)
            )

            Icon(
                painter = painterResource(R.drawable.state_failed),
                contentDescription = null
            )

            Text(
                modifier = modifier.padding(start = 2.dp, end = 8.dp),
                fontSize = 10.sp,
                text = stringResource(R.string.guidance_failed)
            )
        }
    }
}

@Composable
fun VisualizerSnippetCodeDialog(
    modifier: Modifier = Modifier,
    snippetCodeState: MutableState<Boolean>,
    description: String,
    snippetCode: String
) {
    if (snippetCodeState.value)
        AlertDialog(
            modifier = modifier.fillMaxWidth(),
            onDismissRequest = { snippetCodeState.value = false },
            confirmButton = {
                TextButton(
                    onClick = { snippetCodeState.value = false }
                ) {
                    Text(
                        text = stringResource(R.string.visualizer_snippet_code_dialog_close_btn),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {},
            title = {
                Text(
                    text = stringResource(R.string.visualizer_snippet_code_dialog_title),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        text = description
                    )
                    ElevatedCard(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Text(
                            modifier = modifier
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .fillMaxWidth()
                                .padding(16.dp),
                            text = snippetCode
                        )
                    }
                }
            },
            shape = RoundedCornerShape(8.dp),
        )
}


@Preview(showBackground = true)
@Composable
fun ComponentPreview() {
    Scope(
        state = Done(
            ScopeData(
                scopeName = "",
                coroutineName = "",
                thread = "",
                job = Job(),
                parentJob = Job(),
                children = listOf()
            )
        )
    ) { }
}
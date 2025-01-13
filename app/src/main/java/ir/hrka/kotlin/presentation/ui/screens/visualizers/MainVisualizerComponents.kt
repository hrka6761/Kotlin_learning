package ir.hrka.kotlin.presentation.ui.screens.visualizers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.TaskData
import ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities.ThreadData

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
            verticalAlignment = Alignment.CenterVertically
        ) {
            val styledText = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Thread name: ")
                }
                append("${threadData?.threadName ?: "..."}\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Thread id: ")
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val styledText = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Coroutine name: ")
                }
                append("${coroutineData?.coroutineName ?: "..."}\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Thread context: ")
                }
                append("${coroutineData?.threadContext ?: "..."}\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Job context: ")
                }
                append(coroutineData?.jobContext ?: "...")
            }

            Text(
                modifier = Modifier
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
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val styledText = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Task name: ")
                }
                append("${taskData?.taskName ?: "..."}\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Duration time: ")
                }
                append("${taskData?.durationTime ?: "..."} MS")
            }

            Text(
                modifier = Modifier.padding(start = 8.dp, end = 16.dp),
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
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(10.dp)
                    .height(10.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            )

            Text(
                modifier = Modifier.padding(start = 2.dp, end = 10.dp),
                fontSize = 10.sp,
                text = "Thread"
            )

            Box(
                modifier = Modifier
                    .width(10.dp)
                    .height(10.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            )

            Text(
                modifier = Modifier.padding(start = 2.dp, end = 10.dp),
                fontSize = 10.sp,
                text = "Coroutine"
            )

            Box(
                modifier = Modifier
                    .width(10.dp)
                    .height(10.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            )

            Text(
                modifier = Modifier.padding(start = 2.dp, end = 10.dp),
                fontSize = 10.sp,
                text = "Task"
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.state_stop),
                contentDescription = null
            )

            Text(
                modifier = Modifier.padding(start = 2.dp, end = 8.dp),
                fontSize = 10.sp,
                text = "Stop"
            )

            CircularProgressIndicator(
                modifier = Modifier
                    .width(15.dp)
                    .height(15.dp)
                    .padding(end = 2.dp),
                strokeWidth = 1.dp
            )

            Text(
                modifier = Modifier.padding(start = 2.dp, end = 8.dp),
                fontSize = 10.sp,
                text = "Processing"
            )

            Icon(
                painter = painterResource(R.drawable.state_done),
                contentDescription = null
            )

            Text(
                modifier = Modifier.padding(start = 2.dp, end = 8.dp),
                fontSize = 10.sp,
                text = "Done"
            )

            Icon(
                painter = painterResource(R.drawable.state_cancel),
                contentDescription = null
            )

            Text(
                modifier = Modifier.padding(start = 2.dp, end = 8.dp),
                fontSize = 10.sp,
                text = "Cancel"
            )

            Icon(
                painter = painterResource(R.drawable.state_failed),
                contentDescription = null
            )

            Text(
                modifier = Modifier.padding(start = 2.dp, end = 8.dp),
                fontSize = 10.sp,
                text = "Failed"
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ComponentPreview() {}
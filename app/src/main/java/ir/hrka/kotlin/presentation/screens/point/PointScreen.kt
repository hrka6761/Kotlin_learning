package ir.hrka.kotlin.presentation.screens.point

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import ir.hrka.kotlin.R
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.ExecutionState.Stop
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.Point
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.presentation.Failed
import ir.hrka.kotlin.presentation.Loading
import ir.hrka.kotlin.presentation.TopBar

@Composable
fun PointsScreen(
    modifier: Modifier = Modifier,
    topic: Topic?,
    onTopBarBackPressed: () -> Unit,
    appVersionCode: Int,
    navigateToVisualizer: () -> Unit,
    pointsResult: Resource<List<Point>?>,
    updateTopicStateOnDBResult: Resource<Boolean?>,
    setUpdatedTopicId: () -> Unit,
    executionState: ExecutionState,
    failedState: Boolean,
    fetchPoints: () -> Unit,
    onSharePoint: (point: Point) -> Unit,
    onTranslatePoint: (point: Point) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                modifier = modifier,
                title = topic?.topicTitle ?: "",
                navigationClick = { onTopBarBackPressed() },
                actions = {
                    if (
                        topic?.hasVisualizer == true &&
                        appVersionCode >= topic.visualizerVersionCode
                    ) {
                        Icon(
                            modifier = modifier
                                .padding(end = 10.dp)
                                .size(30.dp)
                                .clickable {
                                    navigateToVisualizer()
                                },
                            tint = MaterialTheme.colorScheme.tertiary,
                            painter = painterResource(R.drawable.play_visualizer),
                            contentDescription = ""
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                modifier = modifier.fillMaxWidth(),
                hostState = snackBarHostState
            )
        },
    ) { innerPaddings ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPaddings),
            contentAlignment = Alignment.Center
        ) {
            when (pointsResult) {
                is Resource.Initial -> {}
                is Resource.Loading -> {
                    Loading(
                        modifier = modifier,
                        executionState = executionState
                    )
                }

                is Resource.Success -> {
                    PointsList(
                        modifier = modifier,
                        points = pointsResult.data,
                        onSharePoint = onSharePoint,
                        onTranslatePoint = onTranslatePoint
                    )
                }

                is Resource.Error -> {
                    Failed(
                        modifier = modifier,
                        failedState = failedState
                    )
                }
            }

            LaunchedEffect(Unit) {
                fetchPoints()
            }

            LaunchedEffect(updateTopicStateOnDBResult) {
                if (updateTopicStateOnDBResult is Resource.Success)
                    setUpdatedTopicId()
            }
        }
    }
}

@Composable
fun PointsList(
    modifier: Modifier,
    points: List<Point>?,
    onSharePoint: (point: Point) -> Unit,
    onTranslatePoint: (point: Point) -> Unit
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier.fillMaxWidth(),
        columns = StaggeredGridCells.Fixed(1),
        contentPadding = PaddingValues(8.dp)
    ) {
        points?.let {
            items(it.size) { index ->
                PointItem(
                    modifier = modifier,
                    point = it[index],
                    index = index,
                    onSharePoint = onSharePoint,
                    onTranslatePoint = onTranslatePoint
                )
            }
        }
    }
}

@Composable
fun PointItem(
    modifier: Modifier,
    point: Point,
    index: Int,
    onSharePoint: (point: Point) -> Unit,
    onTranslatePoint: (point: Point) -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        ConstraintLayout {
            val (id, pointHead, subPoints, snippetCodes, shareButton, translateButton) = createRefs()

            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .width(25.dp)
                    .height(25.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(50)
                    )
                    .constrainAs(id) {
                        start.linkTo(parent.start, margin = 8.dp)
                        top.linkTo(parent.top, margin = 8.dp)
                    }
            ) {
                Text(
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    text = (index + 1).toString(),
                )
            }

            IconButton(
                modifier = modifier
                    .constrainAs(shareButton) {
                        end.linkTo(parent.end, margin = 8.dp)
                        top.linkTo(parent.top, margin = 8.dp)
                    },
                onClick = {
                    onSharePoint(point)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null
                )
            }

            IconButton(
                modifier = modifier
                    .constrainAs(translateButton) {
                        end.linkTo(shareButton.start)
                        top.linkTo(parent.top, margin = 8.dp)
                    },
                onClick = {
                    onTranslatePoint(point)
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.translate),
                    contentDescription = null
                )
            }

            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .constrainAs(pointHead) {
                        top.linkTo(shareButton.bottom, margin = 8.dp)
                        start.linkTo(parent.start, margin = 8.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                    },
                textAlign = TextAlign.Start,
                text = point.headPoint,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            if (!point.subPoints.isNullOrEmpty())
                LazyColumn(
                    modifier = modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .constrainAs(subPoints) {
                            top.linkTo(pointHead.bottom, margin = 8.dp)
                            end.linkTo(parent.end, margin = 8.dp)
                            start.linkTo(parent.start, margin = 8.dp)
                            if (point.snippetsCodes.isNullOrEmpty())
                                bottom.linkTo(parent.bottom, margin = 8.dp)
                        }
                ) {
                    items(point.subPoints.size) { index ->
                        SubPintItem(
                            modifier = modifier,
                            subPoints = point.subPoints[index]
                        )
                    }
                }

            if (!point.snippetsCodes.isNullOrEmpty())
                LazyColumn(
                    modifier = modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .constrainAs(snippetCodes) {
                            top.linkTo(
                                if (point.subPoints.isNullOrEmpty()) pointHead.bottom else subPoints.bottom,
                                margin = 8.dp
                            )
                            end.linkTo(parent.end, margin = 8.dp)
                            start.linkTo(parent.start, margin = 8.dp)
                            bottom.linkTo(parent.bottom, margin = 4.dp)
                        }
                ) {
                    items(point.snippetsCodes.size) { index ->
                        SnippetCodeItem(
                            modifier = modifier,
                            snippetCode = point.snippetsCodes[index]
                        )
                    }
                }
        }
    }
}

@Composable
fun SubPintItem(
    modifier: Modifier,
    subPoints: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null
        )

        Text(
            text = subPoints,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun SnippetCodeItem(
    modifier: Modifier,
    snippetCode: String
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(vertical = 12.dp, horizontal = 8.dp),
            text = snippetCode,
            textAlign = TextAlign.Start,
            fontSize = 10.sp,
            lineHeight = 16.sp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PointsScreenPreview() {
    PointsScreen(
        topic = null,
        onTopBarBackPressed = {},
        appVersionCode = 0,
        navigateToVisualizer = {},
        pointsResult = Resource.Success(null),
        updateTopicStateOnDBResult = Resource.Success(null),
        setUpdatedTopicId = {},
        executionState = Stop,
        failedState = false,
        fetchPoints = {},
        onSharePoint = {},
        onTranslatePoint = {}
    )
}
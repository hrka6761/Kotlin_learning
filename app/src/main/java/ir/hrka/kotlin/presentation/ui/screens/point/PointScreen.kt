package ir.hrka.kotlin.presentation.ui.screens.point

import android.app.Activity
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ir.hrka.kotlin.R
import ir.hrka.kotlin.core.Constants.DEFAULT_VERSION_CODE
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.sharePoint
import ir.hrka.kotlin.core.utilities.translatePoint
import ir.hrka.kotlin.domain.entities.Point
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.presentation.MainActivity
import ir.hrka.kotlin.presentation.ui.Failed
import ir.hrka.kotlin.presentation.ui.Loading
import ir.hrka.kotlin.presentation.ui.TopBar

@Composable
fun PointsScreen(
    modifier: Modifier = Modifier,
    activity: Activity,
    navHostController: NavHostController,
    snackBarHostState: SnackbarHostState,
    viewModel: PointsViewModel = hiltViewModel(),
    topic: Topic?,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                modifier = modifier,
                title = topic?.topicTitle ?: "",
                navigationClick = { navHostController.popBackStack() },
                actions = {
                    val appVersionCode = viewModel.getAppVersionCode()

                    if (
                        topic?.hasVisualizer == true &&
                        (appVersionCode ?: DEFAULT_VERSION_CODE) >= topic.visualizerVersionCode
                    ) {
                        Icon(
                            modifier = modifier
                                .padding(end = 10.dp)
                                .size(30.dp)
                                .clickable {
                                    if (topic.visualizerDestination.isNotEmpty())
                                        navHostController.navigate(topic.visualizerDestination)
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
            val points by viewModel.points.collectAsState()
            val failedState by viewModel.failedState.collectAsState()
            val executionState by viewModel.executionState.collectAsState()

            when (points) {
                is Resource.Initial -> {}
                is Resource.Loading -> {
                    Loading(executionState)
                }

                is Resource.Success -> {
                    PointsList(
                        modifier = modifier,
                        activity = activity,
                        points = points.data
                    )
                }

                is Resource.Error -> {
                    Failed(failedState)
                }
            }

            LaunchedEffect(Unit) {
                viewModel.getPoints(topic, navHostController)
            }
        }
    }
}

@Composable
fun PointsList(
    modifier: Modifier,
    activity: Activity,
    points: List<Point>?
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
                    activity = activity,
                    point = it[index],
                    index = index
                )
            }
        }
    }
}

@Composable
fun PointItem(
    modifier: Modifier,
    activity: Activity,
    point: Point,
    index: Int
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
                    activity.sharePoint(point)
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
                    activity.translatePoint(point)
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
    PointItem(
        modifier = Modifier,
        activity = MainActivity(),
        point = Point(
            id = 1,
            headPoint = "This is head Point.",
            subPoints = listOf(
                "This is sub Point 1.",
                "This is sub Point 2."
            ),
            snippetsCodes = listOf(
                " This is sub snippetCode 1.",
                " This is sub snippetCode 2."
            ),
        ),
        index = 1
    )
}
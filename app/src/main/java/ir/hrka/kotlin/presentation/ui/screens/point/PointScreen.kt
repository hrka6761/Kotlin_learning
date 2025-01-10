package ir.hrka.kotlin.presentation.ui.screens.point

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ir.hrka.kotlin.presentation.MainActivity
import ir.hrka.kotlin.R
import ir.hrka.kotlin.core.Constants.TOPICS_SCREEN_UPDATED_TOPIC_STATE_ID_ARGUMENT
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.ExecutionState.Loading
import ir.hrka.kotlin.core.utilities.ExecutionState.Stop
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.Screen.SequentialProgramming
import ir.hrka.kotlin.domain.entities.Point
import ir.hrka.kotlin.domain.entities.db.Topic

@Composable
fun PointsScreen(
    activity: MainActivity,
    navHostController: NavHostController,
    topic: Topic?
) {

    val viewModel: PointViewModel = hiltViewModel()
    val snackBarHostState = remember { SnackbarHostState() }
    val points by viewModel.points.collectAsState()
    val failedState by viewModel.failedState.collectAsState()
    val executionState by viewModel.executionState.collectAsState()
    val updatePointsOnDBResult by viewModel.updatePointsOnDBResult.collectAsState()
    val updateTopicOnDBResult by viewModel.updateTopicStateOnDBResult.collectAsState()


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { PointsScreenAppBar(topic, navHostController) },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .fillMaxWidth(),
                hostState = snackBarHostState
            )
        },
    ) { innerPaddings ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (failedState) 1f else 0f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp),
                    painter = painterResource(R.drawable.error),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    text = "Failed to fetch the data",
                    textAlign = TextAlign.Center
                )
            }

            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPaddings),
                columns = StaggeredGridCells.Fixed(1),
                contentPadding = PaddingValues(8.dp)
            ) {
                val list = points.data

                list?.let {
                    items(it.size) { index ->

                        PointItem(list[index], index)
                    }
                }
            }

            CircularProgressIndicator(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .alpha(if (executionState == Loading) 1f else 0f),
                strokeWidth = 2.dp
            )
        }
    }

    LaunchedEffect(Unit) {
        if (executionState == Start) {
            viewModel.setExecutionState(Loading)
            topic?.let {
                if (topic.hasUpdate)
                    viewModel.getPointsFromGit(it)
                else
                    viewModel.getPointsFromDB(it)
            }
        }
    }

    LaunchedEffect(points) {
        if (executionState != Stop) {
            when (points) {
                is Resource.Initial -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    topic?.let {
                        if (topic.hasUpdate)
                            points.data?.let { viewModel.updatePointsOnDB(topic, it) }
                        else
                            viewModel.setExecutionState(Stop)
                    }

                }

                is Resource.Error -> {
                    viewModel.setExecutionState(Stop)
                    viewModel.setFailedState(true)
                }
            }
        }
    }

    LaunchedEffect(updatePointsOnDBResult) {
        if (executionState != Stop) {
            when (updatePointsOnDBResult) {
                is Resource.Initial -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    topic?.let { viewModel.updateTopicStateOnDB(it) }
                }

                is Resource.Error -> {
                    viewModel.setExecutionState(Stop)
                }
            }
        }
    }

    LaunchedEffect(updateTopicOnDBResult) {
        if (executionState != Stop) {
            when (updateTopicOnDBResult) {
                is Resource.Initial -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    navHostController
                        .previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(TOPICS_SCREEN_UPDATED_TOPIC_STATE_ID_ARGUMENT, topic?.id)
                    viewModel.setExecutionState(Stop)
                }

                is Resource.Error -> {
                    viewModel.setExecutionState(Stop)
                }
            }
        }
    }

    BackHandler {
        navHostController.popBackStack()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PointsScreenAppBar(topic: Topic?, navHostController: NavHostController) {
    TopAppBar(
        title = {
            Text(
                modifier = Modifier.padding(end = 16.dp),
                text = topic?.topicTitle ?: "",
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
            if (topic?.hasVisualizer == true)
                Icon(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(30.dp)
                        .clickable {
                            navHostController.navigate(SequentialProgramming())
                        },
                    tint = MaterialTheme.colorScheme.tertiary,
                    painter = painterResource(R.drawable.play_visualizer),
                    contentDescription = ""
                )
        }
    )
}

@Composable
fun PointItem(point: Point, index: Int) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        ConstraintLayout {
            val (id, pointHead, subPoints, snippetCodes) = createRefs()

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
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

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .constrainAs(pointHead) {
                        top.linkTo(id.bottom, margin = 8.dp)
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
                    modifier = Modifier
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
                        SubPintItem(point.subPoints[index])
                    }
                }

            if (!point.snippetsCodes.isNullOrEmpty())
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .constrainAs(snippetCodes) {
                            top.linkTo(
                                if (point.subPoints.isNullOrEmpty()) pointHead.bottom else subPoints.bottom,
                                margin = 16.dp
                            )
                            end.linkTo(parent.end, margin = 8.dp)
                            start.linkTo(parent.start, margin = 8.dp)
                            bottom.linkTo(parent.bottom, margin = 8.dp)
                        }
                ) {
                    items(point.snippetsCodes.size) { index ->
                        SnippetCodeItem(point.snippetsCodes[index])
                    }
                }
        }
    }
}

@Composable
fun SubPintItem(subPoints: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(Icons.Default.PlayArrow, contentDescription = null)
        Text(
            text = subPoints,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun SnippetCodeItem(snippetCode: String) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Text(
            modifier = Modifier
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
//    PointsScreen(
//        MainActivity(),
//        rememberNavController(),
//        Topic(
//        id = 1,
//            hasUpdate = true,
//            courseName = "Kotlin",
//            fileName = "basic.json",
//            topicTitle = "Basic",
//            pointsNumber = 29,
//            topicImage = "",
//            hasVisualizer = true,
//            isActive = true
//        )
//    )

    PointItem(
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
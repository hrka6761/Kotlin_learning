package ir.hrka.kotlin.presentation.ui.screens.topic

import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ir.hrka.kotlin.presentation.MainActivity
import ir.hrka.kotlin.R
import ir.hrka.kotlin.core.Constants.DEFAULT_VERSION_CODE
import ir.hrka.kotlin.core.Constants.POINTS_SCREEN_TOPIC_ARGUMENT
import ir.hrka.kotlin.core.Constants.TAG
import ir.hrka.kotlin.core.Constants.TOPICS_SCREEN_UPDATED_TOPIC_STATE_ID_ARGUMENT
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.ExecutionState.Loading
import ir.hrka.kotlin.core.utilities.ExecutionState.Stop
import ir.hrka.kotlin.core.utilities.Course
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.Screen.Point
import ir.hrka.kotlin.domain.entities.db.Topic

@Composable
fun TopicsScreen(
    activity: MainActivity,
    navHostController: NavHostController,
    course: Course
) {

    val viewModel: TopicViewModel = hiltViewModel()
    val snackBarHostState = remember { SnackbarHostState() }
    val topics by viewModel.topics.collectAsState()
    val executionState by viewModel.executionState.collectAsState()
    val failedState by viewModel.failedState.collectAsState()
    val updateTopicsOnDBResult by viewModel.updateTopicsOnDBResult.collectAsState()
    val updateTopicsStateOnDBResult by viewModel.updateTopicsStateOnDBResult.collectAsState()
    val updateVersionIdResult by viewModel.updateVersionIdResult.collectAsState()
    val updatedId = navHostController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.get<Int>(TOPICS_SCREEN_UPDATED_TOPIC_STATE_ID_ARGUMENT)

    updatedId?.let { viewModel.updateTopicStateInList(it) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopicsAppBar(navHostController) },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .fillMaxWidth(),
                hostState = snackBarHostState
            )
        }
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
                    text = stringResource(R.string.failed_to_fetch_the_data),
                    textAlign = TextAlign.Center
                )
            }

            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPaddings),
                columns = StaggeredGridCells.Fixed(1),
                contentPadding = PaddingValues(8.dp)
            ) {
                topics.data?.let {
                    items(it.size) { index ->
                        TopicItem(it[index], index, navHostController, viewModel.getAppVersionCode())
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

            if (viewModel.hasTopicsUpdate(course)) {
                viewModel.getTopicsFromGit(course)
                return@LaunchedEffect
            }

            if (viewModel.hasTopicsPointsUpdate(course)) {
                viewModel.updateTopicsStateOnDB(course)
                return@LaunchedEffect
            }

            viewModel.getTopicsFromDB(course)
        }
    }

    LaunchedEffect(topics) {
        if (executionState != Stop) {
            when (topics) {
                is Resource.Initial -> {}
                is Resource.Loading -> {}

                is Resource.Success -> {
                    if (viewModel.hasTopicsUpdate(course))
                        topics.data?.let { viewModel.updateTopicsOnDB(course, it) }
                    else
                        viewModel.setExecutionState(Stop)
                }

                is Resource.Error -> {
                    viewModel.setExecutionState(Stop)
                    viewModel.setFailedState(true)
                }
            }
        }
    }

    LaunchedEffect(updateTopicsOnDBResult) {
        if (executionState != Stop) {
            when (updateTopicsOnDBResult) {
                is Resource.Initial -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    viewModel.updateVersionId(course)
                }

                is Resource.Error -> {
                    viewModel.setExecutionState(Stop)
                }
            }
        }
    }

    LaunchedEffect(updateVersionIdResult) {
        if (executionState != Stop) {
            when (updateVersionIdResult) {
                is Resource.Initial -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    if (viewModel.hasTopicsUpdate(course)) {
                        viewModel.setExecutionState(Stop)
                        viewModel.updateVersionIdInGlobalData(course)
                        return@LaunchedEffect
                    }

                    if (viewModel.hasTopicsPointsUpdate(course)) {
                        viewModel.getTopicsFromDB(course)
                        viewModel.updateVersionIdInGlobalData(course)
                    }
                }

                is Resource.Error -> {
                    if (viewModel.hasTopicsPointsUpdate(course))
                        viewModel.getTopicsFromDB(course)
                    else
                        viewModel.setExecutionState(Stop)
                }
            }
        }
    }

    LaunchedEffect(updateTopicsStateOnDBResult) {
        if (executionState != Stop) {
            when (updateTopicsStateOnDBResult) {
                is Resource.Initial -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    viewModel.updateVersionId(course)
                }

                is Resource.Error -> {
                    viewModel.getTopicsFromDB(course)
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
fun TopicsAppBar(navHostController: NavHostController) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.kotlin_topics_app_bar_title)) },
        navigationIcon = {
            IconButton(
                onClick = { navHostController.popBackStack() }
            ) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
            }
        }
    )
}

@Composable
fun TopicItem(
    topic: Topic,
    index: Int,
    navHostController: NavHostController,
    appVersionCode: Int?
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .alpha(if (topic.isActive) 1f else 0.3f)
                .clickable {
                    if (topic.isActive) {
                        navHostController
                            .currentBackStackEntry
                            ?.savedStateHandle
                            ?.set(POINTS_SCREEN_TOPIC_ARGUMENT, topic)

                        navHostController.navigate(Point())
                    }
                }
                .padding(8.dp)
        ) {
            val (id, data, updateLabel) = createRefs()

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
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            ) {
                Text(
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    text = (index + 1).toString(),
                )
            }

            Column(
                modifier = Modifier.constrainAs(data) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(id.end, margin = 16.dp)
                },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    modifier = Modifier
                        .padding(end = 8.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start,
                    text = topic.topicTitle,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(12.dp),
                        painter = painterResource(R.drawable.point_number),
                        contentDescription = ""
                    )

                    Text(
                        fontSize = 12.sp,
                        text = topic.pointsNumber.toString()
                    )
                }

                if (topic.hasVisualizer)
                    if ((appVersionCode ?: DEFAULT_VERSION_CODE) >= topic.visualizerVersionCode)
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(20.dp),
                                painter = painterResource(R.drawable.visualization),
                                contentDescription = ""
                            )
                            Text(
                                fontSize = 12.sp,
                                text = "Visual"
                            )
                        }
            }

            if (topic.hasUpdate)
                Icon(
                    modifier = Modifier
                        .width(15.dp)
                        .height(15.dp)
                        .constrainAs(updateLabel) {
                            end.linkTo(parent.end, margin = 4.dp)
                            bottom.linkTo(parent.bottom)
                        },
                    painter = painterResource(R.drawable.download_update),
                    contentDescription = null
                )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TopicsScreenPreview() {
    TopicItem(
        Topic(
            id = 1,
            hasUpdate = true,
            courseName = "kotlin",
            topicTitle = "Common classes and functions",
            fileName = "",
            pointsNumber = 29,
            hasVisualizer = true,
            visualizerDestination = "",
            visualizerVersionCode = 0,
            isActive = true
        ),
        14,
        rememberNavController(),
        11
    )
}
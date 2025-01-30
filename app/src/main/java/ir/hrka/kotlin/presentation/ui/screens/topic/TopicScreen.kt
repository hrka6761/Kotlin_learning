package ir.hrka.kotlin.presentation.ui.screens.topic

import android.util.Log
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
import ir.hrka.kotlin.R
import ir.hrka.kotlin.core.Constants.DEFAULT_VERSION_CODE
import ir.hrka.kotlin.core.Constants.POINTS_SCREEN_TOPIC_ARGUMENT
import ir.hrka.kotlin.core.Constants.TAG
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.Screen.Point
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.domain.entities.db.Topic
import ir.hrka.kotlin.presentation.ui.screens.home.Failed
import ir.hrka.kotlin.presentation.ui.screens.home.Loading

@Composable
fun TopicsScreen(
    navHostController: NavHostController,
    course: Course?,
    updatedId: Int?
) {
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopicsAppBar(navHostController, course) },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .fillMaxWidth(),
                hostState = snackBarHostState
            )
        }
    ) { innerPaddings ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings),
            contentAlignment = Alignment.TopCenter
        ) {
            val viewModel: TopicViewModel = hiltViewModel()
            val topics by viewModel.topics.collectAsState()
            val executionState by viewModel.executionState.collectAsState()
            val failedState by viewModel.failedState.collectAsState()

            when (topics) {
                is Resource.Initial -> {}
                is Resource.Loading -> {
                    Loading(executionState)
                }

                is Resource.Success -> {
                    TopicsList(viewModel, navHostController, topics.data)
                }

                is Resource.Error -> {
                    Failed(failedState)
                }
            }

            LaunchedEffect(Unit) {
                viewModel.getTopics(course, updatedId)
            }
        }
    }
}

@Composable
fun TopicsList(
    viewModel: TopicViewModel,
    navHostController: NavHostController,
    topics: List<Topic>?
) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier.fillMaxSize(),
        columns = StaggeredGridCells.Fixed(1),
        contentPadding = PaddingValues(8.dp)
    ) {
        topics?.let {
            items(it.size) { index ->
                TopicItem(it[index], index, navHostController, viewModel.getAppVersionCode())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicsAppBar(
    navHostController: NavHostController,
    course: Course?
) {
    CenterAlignedTopAppBar(
        title = { Text(course?.courseTitle ?: "") },
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
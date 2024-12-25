package ir.hrka.kotlin.presentation.ui.screens.coroutine_topic_points

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.SnackbarDuration
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
import androidx.navigation.compose.rememberNavController
import ir.hrka.kotlin.presentation.MainActivity
import ir.hrka.kotlin.R
import ir.hrka.kotlin.core.Constants.UPDATED_ID_KEY
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.ExecutionState.Loading
import ir.hrka.kotlin.core.utilities.ExecutionState.Stop
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.Screen.SequentialProgramming
import ir.hrka.kotlin.core.utilities.string_utilities.extractFileName
import ir.hrka.kotlin.core.utilities.string_utilities.removeVisualizedFromFileName
import ir.hrka.kotlin.core.utilities.string_utilities.splitByCapitalLetters
import ir.hrka.kotlin.domain.entities.PointData
import kotlinx.coroutines.launch

@Composable
fun CoroutineTopicPointsScreen(
    activity: MainActivity,
    navHostController: NavHostController,
    topicName: String,
    topicId: Int,
    hasContentUpdated: Boolean,
    hasVisualizer: Boolean
) {

    val viewModel: CoroutineTopicPointsViewModel = hiltViewModel()
    val snackBarHostState = remember { SnackbarHostState() }
    val points by viewModel.points.collectAsState()
    val failedState by viewModel.failedState.collectAsState()
    val executionState by viewModel.executionState.collectAsState()
    val saveTopicPointsResult by viewModel.saveTopicPointsResult.collectAsState()
    val updateTopicsOnDBResult by viewModel.updateTopicsOnDBResult.collectAsState()


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { CoroutineTopicPointsScreenAppBar(topicName, navHostController) },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .fillMaxWidth(),
                hostState = snackBarHostState
            )
        },
        bottomBar = {
            if (hasVisualizer)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .clickable {
                            navHostController.navigate(SequentialProgramming())
                        }
                        .padding(8.dp)
                        .navigationBarsPadding(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = stringResource(R.string.visualization_page_title),
                            fontWeight = FontWeight.Bold,
                            fontSize = 36.sp
                        )
                        Icon(
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp),
                            painter = painterResource(R.drawable.play_visualizer),
                            contentDescription = null
                        )
                    }

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp),
                        text = stringResource(R.string.visualization_page_desc),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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
                    text = "Failed to fetch the data",
                    textAlign = TextAlign.Center
                )
            }

            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = innerPaddings.calculateTopPadding(),
                        bottom = innerPaddings.calculateBottomPadding() + 8.dp,
                        start = 8.dp,
                        end = 8.dp
                    ),
                columns = StaggeredGridCells.Fixed(1)
            ) {
                val list = points.data

                list?.let {
                    items(it.size) { index ->
                        CoroutineTopicPointItem(list[index])
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
            if (hasContentUpdated) {
                launch {
                    snackBarHostState.showSnackbar(
                        message = activity.getString(R.string.fetching_new_points_list_msg),
                        duration = SnackbarDuration.Short
                    )
                }
                viewModel.getPointsFromGit(topicName)
            } else
                viewModel.getPointsFromDatabase(topicName)
        }
    }

    LaunchedEffect(points) {
        if (executionState != Stop) {
            when (points) {
                is Resource.Initial -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    if (hasContentUpdated)
                        viewModel.saveTopicPointsOnDB(topicName)
                    else
                        viewModel.setExecutionState(Stop)
                }

                is Resource.Error -> {
                    viewModel.setFailedState(true)
                    viewModel.setExecutionState(Stop)
                    snackBarHostState.showSnackbar(
                        message = points.error?.errorMsg.toString(),
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }

    LaunchedEffect(saveTopicPointsResult) {
        if (executionState != Stop) {
            when (saveTopicPointsResult) {
                is Resource.Initial -> {}
                is Resource.Loading -> {
                    snackBarHostState.showSnackbar(
                        message = activity.getString(R.string.saving_points_on_the_database_msg),
                        duration = SnackbarDuration.Long
                    )
                }

                is Resource.Success -> {
                    viewModel.updateTopicState(topicId)
                }

                is Resource.Error -> {
                    viewModel.setExecutionState(Stop)
                    snackBarHostState.showSnackbar(
                        message = activity.getString(R.string.failed_to_save_points_on_the_database_msg),
                        duration = SnackbarDuration.Long
                    )
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
                    navHostController
                        .previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(UPDATED_ID_KEY, topicId)
                    viewModel.setExecutionState(Stop)
                }

                is Resource.Error -> {
                    snackBarHostState.showSnackbar(
                        message = activity.getString(R.string.failed_to_save_points_on_the_database_msg),
                        duration = SnackbarDuration.Long
                    )
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
fun CoroutineTopicPointsScreenAppBar(topicName: String, navHostController: NavHostController) {
    TopAppBar(
        title = {
            Text(
                text = topicName
                    .extractFileName()
                    .splitByCapitalLetters()
                    .removeVisualizedFromFileName(),
                fontWeight = FontWeight.Bold
            )
        },
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
fun CoroutineTopicPointItem(point: PointData) {
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
                    .width(40.dp)
                    .height(40.dp)
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
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    text = point.num.toString(),
                )
            }

            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .constrainAs(pointHead) {
                        top.linkTo(id.bottom, margin = 8.dp)
                        start.linkTo(parent.start, margin = 8.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                    },
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
                            top.linkTo(pointHead.bottom, margin = 16.dp)
                            end.linkTo(parent.end, margin = 8.dp)
                            start.linkTo(parent.start, margin = 8.dp)
                            if (point.snippetsCode.isNullOrEmpty())
                                bottom.linkTo(parent.bottom, margin = 8.dp)
                        }
                ) {
                    items(point.subPoints.size) { index ->
                        CoroutineTopicSubPintItem(point.subPoints[index])
                    }
                }

            if (!point.snippetsCode.isNullOrEmpty())
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
                    items(point.snippetsCode.size) { index ->
                        CoroutineTopicSnippetCodeItem(point.snippetsCode[index])
                    }
                }
        }
    }
}

@Composable
fun CoroutineTopicSubPintItem(subPoints: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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
fun CoroutineTopicSnippetCodeItem(snippetCode: String) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(vertical = 8.dp),
            text = snippetCode,
            textAlign = TextAlign.Start,
            fontSize = 10.sp,
            lineHeight = 16.sp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun KotlinTopicPointsScreenPreview() {
    CoroutineTopicPointsScreen(
        MainActivity(),
        rememberNavController(),
        "",
        -1,
        hasContentUpdated = false,
        hasVisualizer = true
    )
}
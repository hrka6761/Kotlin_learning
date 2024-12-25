package ir.hrka.kotlin.presentation.ui.screens.coroutine_topics

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.SnackbarDuration
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
import ir.hrka.kotlin.core.utilities.ExecutionState.Loading
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.ExecutionState.Stop
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.Screen.CoroutineTopicPoints
import ir.hrka.kotlin.core.utilities.string_utilities.extractFileName
import ir.hrka.kotlin.core.utilities.string_utilities.removeVisualizedFromFileName
import ir.hrka.kotlin.core.utilities.string_utilities.splitByCapitalLetters
import ir.hrka.kotlin.domain.entities.db.CoroutineTopic

@Composable
fun CoroutineTopicsScreen(
    activity: MainActivity,
    navHostController: NavHostController,
    gitVersionName: String?,
    gitVersionSuffix: String?
) {

    val viewModel: CoroutineTopicsViewModel = hiltViewModel()
    val snackBarHostState = remember { SnackbarHostState() }
    val kotlinTopics by viewModel.coroutineTopics.collectAsState()
    val executionState by viewModel.executionState.collectAsState()
    val failedState by viewModel.failedState.collectAsState()
    val hasUpdateForCoroutineTopicsList by viewModel.hasUpdateForCoroutineTopicsList.collectAsState()
    val hasUpdateForCoroutineTopicsContent by viewModel.hasUpdateForCoroutineTopicsContent.collectAsState()
    val saveCoroutineTopicsListResult by viewModel.saveCoroutineTopicsListResult.collectAsState()
    val updateCoroutineTopicsOnDBResult by viewModel.updateCoroutineTopicsOnDBResult.collectAsState()
    val updatedId = navHostController.currentBackStackEntry
        ?.savedStateHandle
        ?.get<Int>(UPDATED_ID_KEY)


    updatedId?.let { id -> viewModel.updateCoroutineTopicsList(id - 1) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { CoroutineTopicsAppBar(navHostController) },
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
                    text = "Failed to fetch the data",
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
                kotlinTopics.data?.let {
                    items(it.size) { index ->
                        CoroutineTopicItem(it[index], navHostController)
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
            viewModel.checkNewUpdateForCoroutineTopicsList(gitVersionName)
        }
    }

    LaunchedEffect(hasUpdateForCoroutineTopicsList) {
        if (executionState != Stop) {
            if (hasUpdateForCoroutineTopicsList == true)
                viewModel.getCoroutineTopicsFromGit()
            else if (hasUpdateForCoroutineTopicsList == false)
                viewModel.checkNewUpdateForCoroutineTopicsContent(gitVersionName)
        }
    }

    LaunchedEffect(hasUpdateForCoroutineTopicsContent) {
        if (executionState != Stop) {
            if (hasUpdateForCoroutineTopicsContent == true)
                viewModel.updateCoroutineTopicsInDatabase(gitVersionSuffix)
            else if (hasUpdateForCoroutineTopicsContent == false)
                viewModel.getCoroutineTopicsFromDatabase()
        }
    }

    LaunchedEffect(updateCoroutineTopicsOnDBResult) {
        if (executionState != Stop) {
            when (updateCoroutineTopicsOnDBResult) {
                is Resource.Initial -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    viewModel.saveVersionName(gitVersionName!!)
                    viewModel.getCoroutineTopicsFromDatabase()
                }

                is Resource.Error -> {
                    viewModel.setExecutionState(Stop)
                    snackBarHostState.showSnackbar(
                        message = updateCoroutineTopicsOnDBResult.error?.errorMsg ?: "",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    LaunchedEffect(kotlinTopics) {
        if (executionState != Stop) {
            when (kotlinTopics) {
                is Resource.Initial -> {}
                is Resource.Loading -> {
                    if (hasUpdateForCoroutineTopicsList == true) {
                        snackBarHostState.showSnackbar(
                            message = activity.getString(R.string.fetching_new_coroutine_topics_list_msg),
                            duration = SnackbarDuration.Long
                        )
                    }
                }

                is Resource.Success -> {
                    if (kotlinTopics.data?.isEmpty() != false) {
                        viewModel.setExecutionState(Stop)
                        viewModel.setFailedState(true)
                        snackBarHostState.showSnackbar(
                            message = activity.getString(R.string.no_kotlin_topics_msg),
                            duration = SnackbarDuration.Long
                        )
                        return@LaunchedEffect
                    }

                    if (hasUpdateForCoroutineTopicsList == true)
                        viewModel.saveCoroutineTopicsOnDB()
                    else
                        viewModel.setExecutionState(Stop)
                }

                is Resource.Error -> {
                    viewModel.setExecutionState(Stop)
                    viewModel.setFailedState(true)
                    snackBarHostState.showSnackbar(
                        message = kotlinTopics.error?.errorMsg.toString(),
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }

    LaunchedEffect(saveCoroutineTopicsListResult) {
        if (executionState != Stop) {
            when (saveCoroutineTopicsListResult) {
                is Resource.Initial -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    viewModel.saveVersionName(gitVersionName!!)
                    viewModel.setExecutionState(Stop)
                }

                is Resource.Error -> {
                    viewModel.setExecutionState(Stop)
                    snackBarHostState.showSnackbar(
                        message = saveCoroutineTopicsListResult.error?.errorMsg.toString(),
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }

    BackHandler {
        navHostController.popBackStack()
    }
}

@Composable
fun CoroutineTopicItem(
    coroutineTopic: CoroutineTopic,
    navHostController: NavHostController,
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
                .clickable {
                    navHostController.navigate(
                        CoroutineTopicPoints.appendArg(
                            coroutineTopic.name,
                            coroutineTopic.hasUpdated,
                            coroutineTopic.id,
                            coroutineTopic.hasVisualizer
                        )
                    )
                }
                .padding(8.dp)
        ) {
            val (id, title, label) = createRefs()

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(35.dp)
                    .height(35.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(50)
                    )
                    .constrainAs(id) {
                        start.linkTo(parent.start)
                    }
            ) {
                Text(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    text = coroutineTopic.id.toString(),
                )
            }

            Text(
                modifier = Modifier.constrainAs(title) {
                    start.linkTo(id.end, margin = 8.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                text = coroutineTopic.name
                    .extractFileName()
                    .splitByCapitalLetters()
                    .removeVisualizedFromFileName(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            if (coroutineTopic.hasUpdated)
                Icon(
                    modifier = Modifier
                        .width(25.dp)
                        .height(25.dp)
                        .constrainAs(label) {
                            end.linkTo(parent.end, margin = 8.dp)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                    painter = painterResource(R.drawable.download_update),
                    contentDescription = null
                )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoroutineTopicsAppBar(navHostController: NavHostController) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.coroutine_topics_app_bar_title)) },
        navigationIcon = {
            IconButton(
                onClick = { navHostController.popBackStack() }
            ) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun CoroutineTopicsScreenPreview() {
    CoroutineTopicItem(
        CoroutineTopic(
            id = 12,
            name = "Basic sequential programmifdgfdgdfgdfgfdgdrdreyyerte",
            versionName = "0.0.0",
            hasUpdated = true,
            hasVisualizer = true
        ),
        rememberNavController()
    )
}
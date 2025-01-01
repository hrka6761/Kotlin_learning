package ir.hrka.kotlin.presentation.ui.screens.kotlin_topics

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.RequestOptions
import ir.hrka.kotlin.presentation.MainActivity
import ir.hrka.kotlin.R
import ir.hrka.kotlin.core.Constants.TAG
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.ExecutionState.Loading
import ir.hrka.kotlin.core.utilities.ExecutionState.Stop
import ir.hrka.kotlin.core.Constants.UPDATED_ID_KEY
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.Topic

@Composable
fun KotlinTopicsScreen(
    activity: MainActivity,
    navHostController: NavHostController
) {

    val viewModel: KotlinTopicsViewModel = hiltViewModel()
    val snackBarHostState = remember { SnackbarHostState() }
    val kotlinTopics by viewModel.topics.collectAsState()
    val executionState by viewModel.executionState.collectAsState()
    val failedState by viewModel.failedState.collectAsState()
    val saveKotlinTopicsResult by viewModel.saveKotlinTopicsResult.collectAsState()
    val updateKotlinTopicsOnDBResult by viewModel.updateKotlinTopicsOnDBResult.collectAsState()
    val updateKotlinVersionIdResult by viewModel.updateKotlinVersionIdResult.collectAsState()
    val updatedId = navHostController.currentBackStackEntry
        ?.savedStateHandle
        ?.get<Int>(UPDATED_ID_KEY)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { KotlinTopicsAppBar(navHostController) },
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
                kotlinTopics.data?.let {
                    items(it.size) { index ->
                        KotlinTopicItem(it[index], navHostController)
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

            if (viewModel.hasKotlinTopicsUpdate) {
                viewModel.getKotlinTopicsFromGit()
                return@LaunchedEffect
            }

            if (viewModel.hasKotlinTopicsPointsUpdate) {
                viewModel.updateKotlinTopicsOnDB()
                return@LaunchedEffect
            }

            viewModel.getKotlinTopicsFromDB()
        }
    }

    LaunchedEffect(kotlinTopics) {
        if (executionState != Stop) {
            when (kotlinTopics) {
                is Resource.Initial -> {}
                is Resource.Loading -> {}

                is Resource.Success -> {
                    if (viewModel.hasKotlinTopicsUpdate)
                        kotlinTopics.data?.let { viewModel.saveKotlinTopicsOnDB(it) }
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

    LaunchedEffect(saveKotlinTopicsResult) {
        if (executionState != Stop) {
            when (saveKotlinTopicsResult) {
                is Resource.Initial -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    viewModel.updateKotlinVersionId()
                }

                is Resource.Error -> {
                    viewModel.setExecutionState(Stop)
                }
            }
        }
    }

    LaunchedEffect(updateKotlinVersionIdResult) {
        if (executionState != Stop) {
            when (updateKotlinVersionIdResult) {
                is Resource.Initial -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    if (viewModel.hasKotlinTopicsUpdate)
                        viewModel.setExecutionState(Stop)

                    if (viewModel.hasKotlinTopicsPointsUpdate)
                        viewModel.getKotlinTopicsFromDB()

                    viewModel.updateKotlinVersionIdInGlobalData()
                }

                is Resource.Error -> {
                    if (viewModel.hasKotlinTopicsPointsUpdate)
                        viewModel.getKotlinTopicsFromDB()
                    else
                        viewModel.setExecutionState(Stop)
                }
            }
        }
    }

    LaunchedEffect(updateKotlinTopicsOnDBResult) {
        if (executionState != Stop) {
            when (updateKotlinTopicsOnDBResult) {
                is Resource.Initial -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    viewModel.updateKotlinVersionId()
                }

                is Resource.Error -> {
                    viewModel.getKotlinTopicsFromDB()
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
fun KotlinTopicsAppBar(navHostController: NavHostController) {
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun KotlinTopicItem(
    topic: Topic,
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
                .alpha(if (topic.isActive) 1f else 0.3f)
                .clickable {

                }
                .padding(8.dp)
        ) {
            val (id, image, data, updateLabel) = createRefs()

            GlideImage(
                model = topic.topicImage,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(id.end, margin = 8.dp)
                        bottom.linkTo(parent.bottom)
                    },
                requestBuilderTransform = {
                    it.apply(
                        RequestOptions()
                            .error(R.drawable.error)
                    )
                },
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )

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
                        bottom.linkTo(parent.bottom)
                    }
            ) {
                Text(
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    text = topic.topicId.toString(),
                )
            }

            Column(
                modifier = Modifier.constrainAs(data) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(image.end, margin = 8.dp)
                },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    modifier = Modifier
                        .padding(end = 8.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
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
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        text = topic.pointsNumber.toString()
                    )
                }

                if (topic.hasVisualizer)
                    Icon(
                        modifier = Modifier
                            .size(20.dp),
                        painter = painterResource(R.drawable.visualization),
                        contentDescription = ""
                    )
            }

            if (topic.hasUpdate)
                Icon(
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
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
fun KotlinTopicsScreenPreview() {
    KotlinTopicItem(
        Topic(
            topicDBId = 1,
            topicId = 12,
            topicTitle = "Common classes and functions",
            topicImage = "",
            pointsNumber = 29,
            hasVisualizer = false,
            isActive = true,
            hasUpdate = true
        ),
        rememberNavController()
    )
}
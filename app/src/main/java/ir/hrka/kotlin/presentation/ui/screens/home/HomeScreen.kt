package ir.hrka.kotlin.presentation.ui.screens.home

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import ir.hrka.kotlin.core.Constants.SOURCE_URL
import ir.hrka.kotlin.core.Constants.TAG
import ir.hrka.kotlin.core.Constants.TOPICS_SCREEN_COURSE_ARGUMENT
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.Screen.Topic
import ir.hrka.kotlin.core.utilities.Screen.About
import ir.hrka.kotlin.domain.entities.db.Course

@SuppressLint("SwitchIntDef")
@Composable
fun HomeScreen(activity: MainActivity, navHostController: NavHostController) {

    val configuration = LocalConfiguration.current
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { HomeAppBar(navHostController) },
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

            val viewModel: HomeViewModel = hiltViewModel()
            val failedState by viewModel.failedState.collectAsState()
            val courses by viewModel.courses.collectAsState()
            val executionState by viewModel.executionState.collectAsState()

            when (courses) {
                is Resource.Initial -> {}
                is Resource.Loading -> {
                    Loading(executionState)
                }

                is Resource.Success -> {
                    when (configuration.orientation) {
                        ORIENTATION_PORTRAIT -> CoursesColumnList(navHostController, courses.data)
                        ORIENTATION_LANDSCAPE -> CoursesRowList(navHostController, courses.data)
                    }
                }

                is Resource.Error -> {
                    Failed(failedState)
                }
            }

            LaunchedEffect(Unit) {
                viewModel.getCourses()
            }
        }
    }

    BackHandler {
        activity.finish()
    }
}

@Composable
fun Loading(executionState: ExecutionState) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .alpha(if (executionState == ExecutionState.Loading) 1f else 0f)
                .align(Alignment.Center),
            strokeWidth = 2.dp
        )
    }
}

@Composable
fun Failed(failedState: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
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
}

@Composable
fun CoursesColumnList(navHostController: NavHostController, courses: List<Course>?) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 3000.dp)
    ) {
        courses?.let {
            items(it.size) { index ->
                CourseItem(
                    ORIENTATION_PORTRAIT,
                    it[index],
                    navHostController
                )
            }
        }
    }
}

@Composable
fun CoursesRowList(navHostController: NavHostController, courses: List<Course>?) {
    LazyRow(
        modifier = Modifier
            .fillMaxHeight()
            .widthIn(max = 3000.dp)
    ) {
        courses?.let {
            items(it.size) { index ->
                CourseItem(
                    ORIENTATION_LANDSCAPE,
                    it[index],
                    navHostController
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(navHostController: NavHostController) {
    val activity = (LocalContext.current as MainActivity)

    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.home_app_bar_title)) },
        navigationIcon = {
            Image(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(start = 12.dp)
                    .clickable {
                        val customTabsIntent = CustomTabsIntent.Builder()
                            .setShowTitle(true)
                            .setUrlBarHidingEnabled(true)
                            .build()

                        customTabsIntent.launchUrl(activity, Uri.parse(SOURCE_URL))
                    },
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
            )
        },
        actions = {
            IconButton(
                onClick = { navHostController.navigate(About()) }
            ) {
                Icon(
                    modifier = Modifier
                        .width(25.dp)
                        .height(25.dp),
                    painter = painterResource(R.drawable.github),
                    contentDescription = null
                )
            }
        }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CourseItem(
    orientation: Int,
    course: Course,
    navHostController: NavHostController
) {
    ElevatedCard(
        modifier =
        if (orientation == ORIENTATION_PORTRAIT)
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
        else
            Modifier
                .fillMaxHeight()
                .width(400.dp)
                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (img, title, desc, btn) = createRefs()

            GlideImage(
                model = course.courseBanner,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (orientation == ORIENTATION_PORTRAIT) 150.dp else 120.dp)
                    .constrainAs(img) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
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

            Text(
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                    .constrainAs(title) {
                        top.linkTo(img.bottom)
                        start.linkTo(parent.start)
                    },
                text = course.courseTitle,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .constrainAs(desc) {
                        top.linkTo(title.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                text = course.courseDesc,
                style = TextStyle(
                    fontSize = 10.sp,
                    lineHeight = 12.sp
                ),
            )

            Button(
                modifier = Modifier.constrainAs(btn) {
                    top.linkTo(img.bottom)
                    end.linkTo(parent.end, margin = 16.dp)
                    bottom.linkTo(img.bottom)
                },
                onClick = {
                    if (course.isActive) {
                        navHostController
                            .currentBackStackEntry
                            ?.savedStateHandle
                            ?.set(TOPICS_SCREEN_COURSE_ARGUMENT, course)

                        navHostController.navigate(Topic())
                    }
                }
            ) {
                Text(
                    if (course.isActive)
                        stringResource(R.string.home_screen_start_learning_btn)
                    else
                        stringResource(R.string.home_screen_coming_soon_btn)
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
//    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun HomeScreenPreview() {
    HomeScreen(MainActivity(), rememberNavController())
}
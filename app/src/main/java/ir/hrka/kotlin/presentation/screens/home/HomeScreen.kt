package ir.hrka.kotlin.presentation.screens.home

import android.annotation.SuppressLint
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.RequestOptions
import ir.hrka.kotlin.R
import ir.hrka.kotlin.core.errors.BaseError
import ir.hrka.kotlin.core.utilities.ExecutionState
import ir.hrka.kotlin.core.utilities.ExecutionState.Stop
import ir.hrka.kotlin.core.utilities.Result
import ir.hrka.kotlin.domain.entities.db.Course
import ir.hrka.kotlin.presentation.Failed
import ir.hrka.kotlin.presentation.Loading
import ir.hrka.kotlin.presentation.TopBar

@SuppressLint("SwitchIntDef")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToAbout: () -> Unit,
    failedState: Boolean,
    coursesResult: Result<List<Course>?, BaseError>,
    executionState: ExecutionState,
    fetchCourses: () -> Unit,
    onClickCourseRow: (course: Course) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val configuration = LocalConfiguration.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                modifier = modifier,
                title = stringResource(R.string.home_app_bar_title),
                actions = {
                    IconButton(
                        onClick = { navigateToAbout() }
                    ) {
                        Icon(
                            modifier = modifier
                                .width(25.dp)
                                .height(25.dp),
                            painter = painterResource(R.drawable.github),
                            contentDescription = null
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
        }
    ) { innerPaddings ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPaddings),
            contentAlignment = Alignment.TopCenter
        ) {
            when (coursesResult) {
                is Result.Initial, Result.Loading -> {
                    Loading(
                        modifier = modifier,
                        executionState = executionState
                    )
                }

                is Result.Success -> {
                    when (configuration.orientation) {
                        ORIENTATION_PORTRAIT -> CoursesColumnList(
                            modifier = modifier,
                            courses = coursesResult.data,
                            onClickCourseRow = onClickCourseRow
                        )

                        ORIENTATION_LANDSCAPE -> CoursesRowList(
                            modifier = modifier,
                            courses = coursesResult.data,
                            onClickCourseRow = onClickCourseRow
                        )
                    }
                }

                is Result.Error -> {
                    Failed(
                        modifier = modifier,
                        failedState = failedState
                    )
                }
            }

            LaunchedEffect(Unit) {
                fetchCourses()
            }
        }
    }
}

@Composable
fun CoursesColumnList(
    modifier: Modifier = Modifier,
    courses: List<Course>?,
    onClickCourseRow: (course: Course) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 3000.dp)
    ) {
        courses?.let {
            items(it.size) { index ->
                CourseItem(
                    modifier = modifier,
                    ORIENTATION_PORTRAIT,
                    it[index],
                    onClickCourseRow
                )
            }
        }
    }
}

@Composable
fun CoursesRowList(
    modifier: Modifier = Modifier,
    courses: List<Course>?,
    onClickCourseRow: (course: Course) -> Unit
) {
    LazyRow(
        modifier = modifier
            .fillMaxHeight()
            .widthIn(max = 3000.dp)
    ) {
        courses?.let {
            items(it.size) { index ->
                CourseItem(
                    modifier = modifier,
                    ORIENTATION_LANDSCAPE,
                    it[index],
                    onClickCourseRow
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CourseItem(
    modifier: Modifier = Modifier,
    orientation: Int,
    course: Course,
    onClickCourseRow: (course: Course) -> Unit
) {
    ElevatedCard(
        modifier =
            if (orientation == ORIENTATION_PORTRAIT)
                modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            else
                modifier
                    .fillMaxHeight()
                    .width(400.dp)
                    .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        ConstraintLayout(
            modifier = modifier.fillMaxSize()
        ) {
            val (img, title, desc, btn) = createRefs()

            GlideImage(
                model = course.courseBanner,
                modifier = modifier
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
                modifier = modifier
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
                modifier = modifier
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
                modifier = modifier.constrainAs(btn) {
                    top.linkTo(img.bottom)
                    end.linkTo(parent.end, margin = 16.dp)
                    bottom.linkTo(img.bottom)
                },
                onClick = {
                    if (course.isActive)
                        onClickCourseRow(course)
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
    HomeScreen(
        navigateToAbout = {},
        failedState = false,
        coursesResult = Result.Initial,
        executionState = Stop,
        fetchCourses = {},
        onClickCourseRow = {}
    )
}
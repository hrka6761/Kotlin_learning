package ir.hrka.kotlin.presentation.ui.screens.splash

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ir.hrka.kotlin.presentation.MainActivity
import ir.hrka.kotlin.R
import ir.hrka.kotlin.core.utilities.Resource
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.res.stringResource
import ir.hrka.kotlin.core.Constants.FORCE_UPDATE_STATE
import ir.hrka.kotlin.core.Constants.NO_UPDATE_STATE
import ir.hrka.kotlin.core.Constants.TAG
import ir.hrka.kotlin.core.Constants.UPDATE_UNKNOWN_STATE
import ir.hrka.kotlin.core.Constants.UPDATE_STATE
import ir.hrka.kotlin.core.utilities.ExecutionState.Loading
import ir.hrka.kotlin.core.utilities.ExecutionState.Start
import ir.hrka.kotlin.core.utilities.ExecutionState.Stop
import ir.hrka.kotlin.core.utilities.Screen.Home

@Composable
fun SplashScreen(activity: MainActivity, navHostController: NavHostController) {

    val viewModel: SplashViewModel = hiltViewModel()
    val snackBarHostState = remember { SnackbarHostState() }
    val executionState by viewModel.executionState.collectAsState()
    val versionsInfo by viewModel.versionsInfo.collectAsState()
    val updateState by viewModel.updateState.collectAsState()
    val coursesVersionId by viewModel.coursesLocalVersionId.collectAsState()
    val kotlinVersionId by viewModel.kotlinLocalVersionId.collectAsState()
    val coroutineVersionId by viewModel.coroutineLocalVersionId.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .fillMaxWidth(),
                hostState = snackBarHostState
            )
        }
    ) { innerPaddings ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings)
        ) {
            val (logo, title, authorImg, authorName, progressBar) = createRefs()

            Image(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .constrainAs(logo) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
            )

            Text(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(logo.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                text = stringResource(R.string.splash_screen_title)
            )

            Text(
                modifier = Modifier
                    .constrainAs(authorName) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                    },
                text = stringResource(R.string.splash_screen_author_name),
                fontSize = 8.sp
            )

            CircularProgressIndicator(
                modifier = Modifier
                    .width(35.dp)
                    .height(35.dp)
                    .alpha(if (updateState == UPDATE_UNKNOWN_STATE || updateState == NO_UPDATE_STATE) 1f else 0f)
                    .constrainAs(progressBar) {
                        bottom.linkTo(authorName.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                strokeWidth = 1.dp
            )

            Image(
                modifier = Modifier
                    .width(25.dp)
                    .height(25.dp)
                    .constrainAs(authorImg) {
                        bottom.linkTo(authorName.top, margin = 5.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                painter = painterResource(R.drawable.author),
                contentDescription = null,
            )

            if (updateState == UPDATE_STATE || updateState == FORCE_UPDATE_STATE)
                AlertDialog(
                    modifier = Modifier.fillMaxWidth(),
                    onDismissRequest = {},
                    confirmButton = {
                        TextButton(
                            onClick = { viewModel.goToUpdate(activity) }
                        ) {
                            Text(
                                text = stringResource(R.string.new_version_dialog_update_btn),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                if (updateState == FORCE_UPDATE_STATE)
                                    activity.finish()
                                else
                                    navHostController.navigate(Home())
                            }
                        ) {
                            Text(text = stringResource(R.string.new_version_dialog_cancel_btn))
                        }
                    },
                    icon = { Icon(painterResource(R.drawable.update), contentDescription = null) },
                    title = {
                        Text(text = stringResource(R.string.new_version_dialog_title_update))
                    },
                    text = {
                        Column {
                            Text(
                                text = stringResource(R.string.new_version_dialog_desc_available)
                            )
                            if (updateState == FORCE_UPDATE_STATE)
                                Text(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    text = stringResource(R.string.new_version_dialog_desc_available_mandatory_update)
                                )
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 16.dp
                )
        }
    }

    LaunchedEffect(Unit) {
        if (executionState == Start) {
            viewModel.setExecutionState(Loading)
            viewModel.getAppVersions()
        }
    }

    LaunchedEffect(versionsInfo) {
        if (executionState != Stop)
            when (versionsInfo) {
                is Resource.Initial -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    viewModel.getCoursesVersionId()
                }

                is Resource.Error -> {
                    viewModel.getCoursesVersionId()
                }
            }
    }

    LaunchedEffect(coursesVersionId) {
        if (executionState != Stop)
            when (coursesVersionId) {
                is Resource.Initial -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    viewModel.getKotlinVersionId()
                }

                is Resource.Error -> {
                    viewModel.getKotlinVersionId()
                }
            }
    }

    LaunchedEffect(kotlinVersionId) {
        if (executionState != Stop)
            when (kotlinVersionId) {
                is Resource.Initial -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    viewModel.getCoroutineVersionId()
                }

                is Resource.Error -> {
                    viewModel.getCoroutineVersionId()
                }
            }
    }

    LaunchedEffect(coroutineVersionId) {
        if (executionState != Stop)
            when (coroutineVersionId) {
                is Resource.Initial -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    viewModel.initGlobalData()
                    viewModel.checkNewVersion(activity)
                }

                is Resource.Error -> {
                    viewModel.initGlobalData()
                    viewModel.checkNewVersion(activity)
                }
            }
    }

    LaunchedEffect(updateState) {
        if (executionState != Stop && updateState == NO_UPDATE_STATE) {
            viewModel.setExecutionState(Stop)
            navHostController.navigate(Home())
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(MainActivity(), rememberNavController())
}
package ir.hrka.kotlin.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
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
import ir.hrka.kotlin.MainActivity
import ir.hrka.kotlin.R
import ir.hrka.kotlin.core.Constants.MANDATORY
import ir.hrka.kotlin.core.Constants.NEW_VERSION_CANCEL
import ir.hrka.kotlin.core.Constants.NEW_VERSION_NOT_AVAILABLE
import ir.hrka.kotlin.core.Constants.NEW_VERSION_CONTINUE
import ir.hrka.kotlin.core.Constants.NEW_VERSION_AVAILABLE
import ir.hrka.kotlin.core.Constants.NEW_VERSION_UNKNOWN_STATE
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.Screen.Home
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.res.stringResource
import ir.hrka.kotlin.core.ExecutionState.Loading
import ir.hrka.kotlin.core.ExecutionState.Start
import ir.hrka.kotlin.core.ExecutionState.Stop

@Composable
fun SplashScreen(activity: MainActivity, navHostController: NavHostController) {

    val viewModel: SplashViewModel = hiltViewModel()
    val snackBarHostState = remember { SnackbarHostState() }
    val executionState by viewModel.executionState.collectAsState()
    val appInfo by viewModel.appInfo.collectAsState()
    val newVersionState by viewModel.newVersionState.collectAsState()

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
                    .alpha(
                        if (newVersionState == NEW_VERSION_AVAILABLE ||
                            newVersionState == NEW_VERSION_CONTINUE
                        ) 0f else 1f
                    )
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

            if (newVersionState == NEW_VERSION_AVAILABLE || newVersionState == NEW_VERSION_CONTINUE)
                AlertDialog(
                    modifier = Modifier.fillMaxWidth(),
                    onDismissRequest = {},
                    confirmButton = {
                        TextButton(
                            onClick = { viewModel.setNewVersionDialogState(NEW_VERSION_CONTINUE) }
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
                                viewModel.setNewVersionDialogState(NEW_VERSION_CANCEL)
                            }
                        ) { Text(text = stringResource(R.string.new_version_dialog_cancel_btn)) }
                    },
                    icon = { Icon(painterResource(R.drawable.update), contentDescription = null) },
                    title = {
                        Text(text = stringResource(R.string.new_version_dialog_title_update))
                    },
                    text = {
                        Text(
                            stringResource(
                                R.string.new_version_dialog_desc_available,
                                if (appInfo.data?.versionNameSuffix == MANDATORY)
                                    stringResource(R.string.new_version_dialog_desc_available_mandatory_update)
                                else
                                    ""
                            )
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 16.dp
                )
        }
    }

    LaunchedEffect(Unit) {
        if (executionState == Start)
            viewModel.getAppInfo()
    }

    LaunchedEffect(appInfo) {
        if (executionState != Stop)
            when (appInfo) {
                is Resource.Initial -> {
                    viewModel.setExecutionState(Loading)
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    viewModel.checkNewVersion(activity)
                }

                is Resource.Error -> {
                    viewModel.setNewVersionDialogState(NEW_VERSION_UNKNOWN_STATE)
                }
            }
    }

    LaunchedEffect(newVersionState) {
        if (executionState != Stop) {
            if (newVersionState == NEW_VERSION_NOT_AVAILABLE) {
                viewModel.setExecutionState(Stop)
                delay(500)
                navHostController.navigate(
                    Home.appendArg(
                        appInfo.data?.versionName ?: "",
                        appInfo.data?.versionNameSuffix ?: ""
                    )
                )
            } else if (newVersionState == NEW_VERSION_UNKNOWN_STATE) {
                snackBarHostState.showSnackbar(
                    message = appInfo.error?.errorMsg.toString(),
                    duration = SnackbarDuration.Short
                )
                viewModel.setExecutionState(Stop)
                navHostController.navigate(
                    Home.appendArg(
                        appInfo.data?.versionName ?: "",
                        appInfo.data?.versionNameSuffix ?: ""
                    )
                )
            } else if (newVersionState == NEW_VERSION_CANCEL) {
                viewModel.setExecutionState(Stop)
                if (appInfo.data?.versionNameSuffix == MANDATORY)
                    activity.finish()
                else
                    navHostController.navigate(
                        Home.appendArg(
                            appInfo.data?.versionName ?: "",
                            appInfo.data?.versionNameSuffix ?: ""
                        )
                    )
            } else if (newVersionState == NEW_VERSION_CONTINUE) {
                viewModel.setNewVersionDialogState(NEW_VERSION_AVAILABLE)
                viewModel.goToUpdate(activity)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(MainActivity(), rememberNavController())
}
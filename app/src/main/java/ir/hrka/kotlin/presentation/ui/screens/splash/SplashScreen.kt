package ir.hrka.kotlin.presentation.ui.screens.splash

import android.content.Intent
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.ui.res.stringResource
import ir.hrka.kotlin.core.Constants.BAZAAR_URL
import ir.hrka.kotlin.core.Constants.FORCE_UPDATE_STATE
import ir.hrka.kotlin.core.Constants.NO_UPDATE_STATE
import ir.hrka.kotlin.core.Constants.UPDATE_UNKNOWN_STATE
import ir.hrka.kotlin.core.Constants.UPDATE_STATE
import ir.hrka.kotlin.core.utilities.ExecutionState.Stop
import ir.hrka.kotlin.core.utilities.Screen.Home
import androidx.core.net.toUri
import ir.hrka.kotlin.core.utilities.Screen.Splash
import ir.hrka.kotlin.data.datasource.preference.PreferenceDataSource
import ir.hrka.kotlin.data.repositories.preference.ReadPreferencesRepoImpl
import ir.hrka.kotlin.di.GlobalModule
import ir.hrka.kotlin.domain.usecases.preference.GetKotlinVersionIdUseCase
import ir.hrka.kotlin.presentation.GlobalData
import kotlinx.coroutines.Dispatchers

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    activity: MainActivity,
    navHostController: NavHostController,
    snackBarHostState: SnackbarHostState,
    viewModel: SplashViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                modifier = modifier.fillMaxWidth(),
                hostState = snackBarHostState
            )
        }
    ) { innerPaddings ->
        ConstraintLayout(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPaddings)
        ) {
            val (logo, title, authorImg, authorName, progressBar) = createRefs()
            val executionState by viewModel.executionState.collectAsState()
            val updateState by viewModel.updateState.collectAsState()

            Image(
                modifier = modifier
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
                modifier = modifier
                    .constrainAs(title) {
                        top.linkTo(logo.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                text = stringResource(R.string.splash_screen_title)
            )

            Text(
                modifier = modifier
                    .constrainAs(authorName) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                    },
                text = stringResource(R.string.splash_screen_author_name),
                fontSize = 8.sp
            )

            Image(
                modifier = modifier
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

            CircularProgressIndicator(
                Modifier
                    .size(35.dp)
                    .alpha(if (updateState == UPDATE_UNKNOWN_STATE || updateState == NO_UPDATE_STATE) 1f else 0f)
                    .constrainAs(progressBar) {
                        bottom.linkTo(authorName.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                strokeWidth = 1.dp
            )

            if (updateState == UPDATE_STATE || updateState == FORCE_UPDATE_STATE)
                AlertDialog(
                    modifier = modifier.fillMaxWidth(),
                    onDismissRequest = {},
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, BAZAAR_URL.toUri())
                                activity.startActivity(intent)
                            }
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
                                    navHostController.navigate(Home.destination)
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

            LaunchedEffect(Unit) {
                viewModel.checkChangelog(activity)
            }

            LaunchedEffect(updateState) {
                if (executionState != Stop && updateState == NO_UPDATE_STATE)
                    navHostController.navigate(Home.destination) {
                        popUpTo(Splash.destination) { inclusive = true }
                    }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(
        activity = MainActivity(),
        navHostController = rememberNavController(),
        snackBarHostState = SnackbarHostState()
    )
}
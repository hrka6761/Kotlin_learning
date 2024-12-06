package ir.hrka.kotlin.ui.screens.profile

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ir.hrka.kotlin.MainActivity
import ir.hrka.kotlin.R
import ir.hrka.kotlin.core.Constants.CLIPBOARD_LABEL
import ir.hrka.kotlin.core.Constants.CLIPBOARD_TEXT
import ir.hrka.kotlin.core.Constants.GITHUB_URL
import ir.hrka.kotlin.core.Constants.LINKEDIN_URL
import ir.hrka.kotlin.core.Constants.SOURCE_URL
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column

@SuppressLint("SwitchIntDef")
@Composable
fun ProfileScreen(activity: MainActivity, navHostController: NavHostController) {

    val viewModel: ProfileViewModel = hiltViewModel()
    val configuration = LocalConfiguration.current
    val snackBarHostState = remember { SnackbarHostState() }


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = { ProfileAppBar(navHostController) },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .fillMaxWidth(),
                hostState = snackBarHostState
            )
        }
    ) { innerPaddings ->

        when (configuration.orientation) {
            ORIENTATION_PORTRAIT -> PortraitScreen(viewModel, activity, innerPaddings)
            ORIENTATION_LANDSCAPE -> LandscapeScreen(viewModel, activity, innerPaddings)
        }
    }

    BackHandler {
        navHostController.popBackStack()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAppBar(navHostController: NavHostController) {
    TopAppBar(
        title = {
            Text(
                text = "Author info",
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
fun PortraitScreen(
    viewModel: ProfileViewModel,
    activity: MainActivity,
    innerPaddings: PaddingValues
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddings)
    ) {
        val (authorImg, authorName, authorEmail, githubRepo, source, linkedin) = createRefs()

        Image(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .constrainAs(authorImg) {
                    top.linkTo(parent.top, margin = 32.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            painter = painterResource(R.drawable.author),
            contentDescription = null,
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(authorName) {
                    top.linkTo(authorImg.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            text = stringResource(R.string.profile_screen_author_name),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(authorEmail) {
                    top.linkTo(authorName.bottom, margin = 64.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end, margin = 8.dp)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(start = 16.dp),
                contentDescription = null,
                imageVector = Icons.Default.Email
            )

            Text(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable {
                        val clipboard =
                            activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText(CLIPBOARD_LABEL, CLIPBOARD_TEXT)
                        clipboard.setPrimaryClip(clip)
                    },
                text = stringResource(R.string.profile_screen_author_email),
                textAlign = TextAlign.Start,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(githubRepo) {
                    top.linkTo(authorEmail.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end, margin = 8.dp)
                }
                .clickable { viewModel.openUrl(activity, GITHUB_URL) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(start = 16.dp),
                contentDescription = null,
                painter = painterResource(R.drawable.github)
            )

            Text(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = stringResource(R.string.profile_screen_github_repo_address),
                textAlign = TextAlign.Start,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(source) {
                    top.linkTo(githubRepo.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end, margin = 8.dp)
                }
                .clickable { viewModel.openUrl(activity, SOURCE_URL) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(start = 16.dp),
                contentDescription = null,
                imageVector = Icons.Default.Info
            )

            Text(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = stringResource(R.string.profile_screen_cheatSheet_source),
                textAlign = TextAlign.Start,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(linkedin) {
                    top.linkTo(source.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end, margin = 8.dp)
                }
                .clickable { viewModel.openUrl(activity, LINKEDIN_URL) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(start = 16.dp),
                contentDescription = null,
                painter = painterResource(R.drawable.linkedin)
            )

            Text(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = stringResource(R.string.profile_screen_linkedin_address),
                textAlign = TextAlign.Start,
            )
        }
    }
}

@Composable
fun LandscapeScreen(
    viewModel: ProfileViewModel,
    activity: MainActivity,
    innerPaddings: PaddingValues
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddings)
    ) {
        val (authorImg, authorInfo) = createRefs()

        Column(
            modifier = Modifier
                .constrainAs(authorImg) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, margin = 64.dp)
                    bottom.linkTo(parent.bottom)
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp),
                painter = painterResource(R.drawable.author),
                contentDescription = null,
            )

            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = stringResource(R.string.profile_screen_author_name),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .constrainAs(authorInfo) {
                    top.linkTo(parent.top)
                    start.linkTo(authorImg.end, margin = 64.dp)
                    bottom.linkTo(parent.bottom)
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .padding(start = 16.dp),
                    contentDescription = null,
                    imageVector = Icons.Default.Email
                )

                Text(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .clickable {
                            val clipboard =
                                activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText(CLIPBOARD_LABEL, CLIPBOARD_TEXT)
                            clipboard.setPrimaryClip(clip)
                        },
                    text = stringResource(R.string.profile_screen_author_email),
                    textAlign = TextAlign.Start,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openUrl(activity, GITHUB_URL) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .padding(start = 16.dp),
                    contentDescription = null,
                    painter = painterResource(R.drawable.github)
                )

                Text(
                    modifier = Modifier
                        .padding(start = 16.dp),
                    text = stringResource(R.string.profile_screen_github_repo_address),
                    textAlign = TextAlign.Start,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openUrl(activity, SOURCE_URL) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .padding(start = 16.dp),
                    contentDescription = null,
                    imageVector = Icons.Default.Info
                )

                Text(
                    modifier = Modifier
                        .padding(start = 16.dp),
                    text = stringResource(R.string.profile_screen_cheatSheet_source),
                    textAlign = TextAlign.Start,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openUrl(activity, LINKEDIN_URL) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .padding(start = 16.dp),
                    contentDescription = null,
                    painter = painterResource(R.drawable.linkedin)
                )

                Text(
                    modifier = Modifier
                        .padding(start = 16.dp),
                    text = stringResource(R.string.profile_screen_linkedin_address),
                    textAlign = TextAlign.Start,
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
fun ProfileScreenPreview() {
    ProfileScreen(MainActivity(), rememberNavController())
}
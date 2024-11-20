package ir.hrka.kotlin.ui.screens.about

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
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
import ir.hrka.kotlin.core.utilities.Constants.CLIPBOARD_LABEL
import ir.hrka.kotlin.core.utilities.Constants.CLIPBOARD_TEXT
import ir.hrka.kotlin.core.utilities.Constants.GITHUB_URL
import ir.hrka.kotlin.core.utilities.Constants.LINKEDIN_URL
import ir.hrka.kotlin.core.utilities.Constants.SOURCE_URL

@Composable
fun ProfileScreen(activity: MainActivity, navHostController: NavHostController) {

    val viewModel: ProfileViewModel = hiltViewModel()
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

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

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings)
                .verticalScroll(rememberScrollState())
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
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
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


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(MainActivity(), rememberNavController())
}
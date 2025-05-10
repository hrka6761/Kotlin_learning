package ir.hrka.kotlin.presentation.screens.about

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import ir.hrka.kotlin.R
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import ir.hrka.kotlin.core.Constants.GITHUB_URL
import ir.hrka.kotlin.core.Constants.LINKEDIN_URL
import ir.hrka.kotlin.core.Constants.SOURCE_URL
import ir.hrka.kotlin.presentation.TopBar

@SuppressLint("SwitchIntDef")
@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    onTopBarBackPressed: () -> Unit,
    onClickEmail: () -> Unit,
    openUrl: (url: String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                modifier = modifier,
                title = stringResource(R.string.about_app_bar_title),
                navigationClick = { onTopBarBackPressed() }
            )
        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier.fillMaxWidth(),
                hostState = snackBarHostState
            )
        }
    ) { innerPaddings ->

        when (configuration.orientation) {
            ORIENTATION_PORTRAIT -> PortraitScreen(
                innerPaddings = innerPaddings,
                onClickEmail = onClickEmail,
                openUrl = openUrl
            )

            ORIENTATION_LANDSCAPE -> LandscapeScreen(
                innerPaddings = innerPaddings,
                onClickEmail = onClickEmail,
                openUrl = openUrl
            )
        }
    }
}

@Composable
fun PortraitScreen(
    innerPaddings: PaddingValues,
    onClickEmail: () -> Unit,
    openUrl: (url: String) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddings)
    ) {
        val (banner, authorImg, authorName, authorEmail, githubRepo, source, linkedin) = createRefs()

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(banner) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.profile_banner),
            contentDescription = null,
        )

        Box(
            modifier = Modifier
                .width(80.dp)
                .height(80.dp)
                .constrainAs(authorImg) {
                    top.linkTo(banner.bottom)
                    start.linkTo(parent.start, margin = 16.dp)
                    bottom.linkTo(banner.bottom)
                }
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = CircleShape
                )
        ) {
            Image(
                painter = painterResource(R.drawable.author),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(authorName) {
                    start.linkTo(parent.start, margin = 8.dp)
                    end.linkTo(parent.end, margin = 8.dp)
                    top.linkTo(authorImg.bottom, margin = 16.dp)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(start = 8.dp),
                contentDescription = null,
                imageVector = Icons.Default.Person
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = stringResource(R.string.profile_screen_author_name),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(authorEmail) {
                    start.linkTo(parent.start, margin = 8.dp)
                    end.linkTo(parent.end, margin = 8.dp)
                    top.linkTo(authorName.bottom, margin = 8.dp)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(start = 8.dp),
                contentDescription = null,
                imageVector = Icons.Default.Email
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable {
                        onClickEmail()
                    },
                text = stringResource(R.string.profile_screen_author_email),
                textAlign = TextAlign.Start,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(githubRepo) {
                    start.linkTo(parent.start, margin = 8.dp)
                    end.linkTo(parent.end, margin = 8.dp)
                    top.linkTo(authorEmail.bottom, margin = 8.dp)
                }
                .clickable {
                    openUrl(GITHUB_URL)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(start = 8.dp),
                contentDescription = null,
                painter = painterResource(R.drawable.github)
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                text = stringResource(R.string.profile_screen_github_repo_address),
                textAlign = TextAlign.Start,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(source) {
                    start.linkTo(parent.start, margin = 8.dp)
                    end.linkTo(parent.end, margin = 8.dp)
                    top.linkTo(githubRepo.bottom, margin = 8.dp)
                }
                .clickable {
                    openUrl(SOURCE_URL)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(start = 8.dp),
                contentDescription = null,
                imageVector = Icons.Default.Info
            )

            Text(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = stringResource(R.string.profile_screen_kotlin_topics_source),
                textAlign = TextAlign.Start,
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(linkedin) {
                    start.linkTo(parent.start, margin = 8.dp)
                    end.linkTo(parent.end, margin = 8.dp)
                    top.linkTo(source.bottom, margin = 8.dp)
                }
                .clickable {
                    openUrl(LINKEDIN_URL)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(start = 8.dp),
                contentDescription = null,
                painter = painterResource(R.drawable.linkedin)
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                text = stringResource(R.string.profile_screen_linkedin_address),
                textAlign = TextAlign.Start,
            )
        }
    }
}

@Composable
fun LandscapeScreen(
    innerPaddings: PaddingValues,
    onClickEmail: () -> Unit,
    openUrl: (url: String) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddings)
    ) {
        val (banner, authorImg, authorName, authorEmail, githubRepo, source, linkedin) = createRefs()

        Image(
            modifier = Modifier
                .fillMaxHeight()
                .width(320.dp)
                .constrainAs(banner) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            contentScale = ContentScale.FillHeight,
            painter = painterResource(R.drawable.profile_banner),
            contentDescription = null,
        )

        Box(
            modifier = Modifier
                .width(80.dp)
                .height(80.dp)
                .constrainAs(authorImg) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(banner.end)
                    end.linkTo(banner.end)
                }
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = CircleShape
                )
        ) {
            Image(
                painter = painterResource(R.drawable.author),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(authorName) {
                    start.linkTo(authorImg.end, margin = 8.dp)
                    top.linkTo(authorImg.top, margin = 32.dp)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(start = 8.dp),
                contentDescription = null,
                imageVector = Icons.Default.Person
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = stringResource(R.string.profile_screen_author_name),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(authorEmail) {
                    start.linkTo(authorImg.end, margin = 8.dp)
                    top.linkTo(authorName.bottom)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(start = 8.dp),
                contentDescription = null,
                imageVector = Icons.Default.Email
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable {
                        onClickEmail()
                        /*val clipboard =
                            activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText(CLIPBOARD_LABEL, CLIPBOARD_TEXT)
                        clipboard.setPrimaryClip(clip)*/
                    },
                text = stringResource(R.string.profile_screen_author_email),
                textAlign = TextAlign.Start,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(githubRepo) {
                    start.linkTo(authorImg.end, margin = 8.dp)
                    top.linkTo(authorEmail.bottom)
                }
                .clickable {
                    openUrl(GITHUB_URL)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(start = 8.dp),
                contentDescription = null,
                painter = painterResource(R.drawable.github)
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                text = stringResource(R.string.profile_screen_github_repo_address),
                textAlign = TextAlign.Start,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(source) {
                    start.linkTo(authorImg.end, margin = 8.dp)
                    top.linkTo(githubRepo.bottom)
                }
                .clickable {
                    openUrl(SOURCE_URL)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(start = 8.dp),
                contentDescription = null,
                imageVector = Icons.Default.Info
            )

            Text(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = stringResource(R.string.profile_screen_kotlin_topics_source),
                textAlign = TextAlign.Start,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(linkedin) {
                    start.linkTo(authorImg.end, margin = 8.dp)
                    top.linkTo(source.bottom)
                }
                .clickable {
                    openUrl(LINKEDIN_URL)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(start = 8.dp),
                contentDescription = null,
                painter = painterResource(R.drawable.linkedin)
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                text = stringResource(R.string.profile_screen_linkedin_address),
                textAlign = TextAlign.Start,
            )
        }
    }
}


@Preview(
    showBackground = true,
//    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun AboutScreenPreview() {
    AboutScreen(
        onTopBarBackPressed = {},
        onClickEmail = {},
        openUrl = {}
    )
}
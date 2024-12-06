package ir.hrka.kotlin.ui.screens.home

import android.annotation.SuppressLint
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ir.hrka.kotlin.MainActivity
import ir.hrka.kotlin.R
import ir.hrka.kotlin.core.utilities.Screen.CheatSheet
import ir.hrka.kotlin.core.utilities.Screen.Profile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("SwitchIntDef")
@Composable
fun HomeScreen(
    activity: MainActivity,
    navHostController: NavHostController,
    githubVersionName: String?,
    githubVersionSuffix: String?
) {

    val viewModel: HomeViewModel = hiltViewModel()
    val snackBarHostState = remember { SnackbarHostState() }
    val configuration = LocalConfiguration.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

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
        when (configuration.orientation) {
            ORIENTATION_PORTRAIT -> PortraitScreen(
                activity,
                scope,
                navHostController,
                innerPaddings,
                scrollState,
                githubVersionName,
                githubVersionSuffix,
                snackBarHostState
            )

            ORIENTATION_LANDSCAPE -> LandscapeScreen(
                activity,
                scope,
                navHostController,
                innerPaddings,
                scrollState,
                githubVersionName,
                githubVersionSuffix,
                snackBarHostState
            )
        }
    }

    BackHandler {
        activity.finish()
    }
}

@Composable
fun PortraitScreen(
    activity: MainActivity,
    scope: CoroutineScope,
    navHostController: NavHostController,
    innerPaddings: PaddingValues,
    scrollState: ScrollState,
    githubVersionName: String?,
    githubVersionSuffix: String?,
    snackBarHostState: SnackbarHostState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPaddings)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .clickable {
                    navHostController.navigate(
                        CheatSheet.appendArg(
                            githubVersionName ?: "",
                            githubVersionSuffix ?: ""
                        )
                    )
                },
            elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                painter = painterResource(R.drawable.kotlin),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
                text = stringResource(R.string.home_screen_kotlin_learning_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                text = stringResource(R.string.home_screen_kotlin_learning_description),
                style = TextStyle(
                    fontSize = 10.sp,
                    lineHeight = 12.sp
                ),
                fontWeight = FontWeight.Bold,
            )
        }

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .alpha(0.2f)
                .clickable {
                    scope.launch {
                        snackBarHostState.showSnackbar(
                            message = activity.getString(R.string.home_screen_coming_soon_msg),
                            duration = SnackbarDuration.Short
                        )
                    }
                },
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                painter = painterResource(R.drawable.coroutine),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
                text = stringResource(R.string.home_screen_coroutine_learning_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                text = stringResource(R.string.home_screen_coroutine_learning_description),
                style = TextStyle(
                    fontSize = 10.sp,
                    lineHeight = 12.sp
                ),
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun LandscapeScreen(
    activity: MainActivity,
    scope: CoroutineScope,
    navHostController: NavHostController,
    innerPaddings: PaddingValues,
    scrollState: ScrollState,
    githubVersionName: String?,
    githubVersionSuffix: String?,
    snackBarHostState: SnackbarHostState
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddings)
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElevatedCard(
            modifier = Modifier
                .width(400.dp)
                .fillMaxHeight()
                .padding(vertical = 16.dp, horizontal = 16.dp)
                .clickable {
                    navHostController.navigate(
                        CheatSheet.appendArg(
                            githubVersionName ?: "",
                            githubVersionSuffix ?: ""
                        )
                    )
                },
            elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
        ) {
            Image(
                modifier = Modifier.height(120.dp),
                painter = painterResource(R.drawable.kotlin),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )

            Text(
                modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
                text = stringResource(R.string.home_screen_kotlin_learning_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                text = stringResource(R.string.home_screen_kotlin_learning_description),
                style = TextStyle(
                    fontSize = 10.sp,
                    lineHeight = 12.sp
                ),
                fontWeight = FontWeight.Bold,
            )
        }

        ElevatedCard(
            modifier = Modifier
                .width(400.dp)
                .fillMaxHeight()
                .padding(vertical = 16.dp, horizontal = 16.dp)
                .alpha(0.2f)
                .clickable {
                    scope.launch {
                        snackBarHostState.showSnackbar(
                            message = activity.getString(R.string.home_screen_coming_soon_msg),
                            duration = SnackbarDuration.Short
                        )
                    }
                }
        ) {
            Image(
                modifier = Modifier.height(120.dp),
                painter = painterResource(R.drawable.coroutine),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )

            Text(
                modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
                text = stringResource(R.string.home_screen_coroutine_learning_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                text = stringResource(R.string.home_screen_coroutine_learning_description),
                style = TextStyle(
                    fontSize = 10.sp,
                    lineHeight = 12.sp
                ),
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(navHostController: NavHostController) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.home_app_bar_title)) },
        navigationIcon = {
            Image(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .padding(start = 12.dp),
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
            )
        },
        actions = {
            IconButton(
                onClick = { navHostController.navigate(Profile()) }
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

@Preview(
    showBackground = true,
//    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun HomeScreenPreview() {
    HomeScreen(MainActivity(), rememberNavController(), "", "")
}
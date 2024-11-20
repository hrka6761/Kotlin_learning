package ir.hrka.kotlin.ui.screens.home

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ir.hrka.kotlin.MainActivity
import ir.hrka.kotlin.R
import ir.hrka.kotlin.core.utilities.Constants.TAG
import ir.hrka.kotlin.core.utilities.Constants.UPDATED_ID_KEY
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.Screen
import ir.hrka.kotlin.core.utilities.extractFileName
import ir.hrka.kotlin.core.utilities.splitByCapitalLetters
import ir.hrka.kotlin.domain.entities.RepoFileModel

@Composable
fun HomeScreen(
    activity: MainActivity,
    navHostController: NavHostController,
    githubVersionName: String?,
    githubVersionSuffix: String?
) {

    val viewModel: HomeViewModel = hiltViewModel()
    val snackBarHostState = remember { SnackbarHostState() }
    val cheatSheets by viewModel.cheatSheets.collectAsState()
    val progressBarState by viewModel.progressBarState.collectAsState()
    val hasUpdateForCheatSheetsList by viewModel.hasUpdateForCheatSheetsList.collectAsState()
    val hasUpdateForCheatSheetsContent by viewModel.hasUpdateForCheatSheetsContent.collectAsState()
    val saveCheatsheetsListResult by viewModel.saveCheatsheetsListResult.collectAsState()
    val updateCheatsheetsOnDBResult by viewModel.updateCheatsheetsOnDBResult.collectAsState()
    val updatedId = navHostController.currentBackStackEntry
        ?.savedStateHandle
        ?.get<Int>(UPDATED_ID_KEY)

    updatedId?.let { id -> viewModel.updateCheatSheetsList(id - 1) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { HomeAppBar() },
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
            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPaddings),
                columns = StaggeredGridCells.Fixed(1),
                contentPadding = PaddingValues(8.dp)
            ) {
                cheatSheets.data?.let {
                    items(it.size) { index ->
                        CheatSheetItem(it[index], navHostController)
                    }
                }
            }

            CircularProgressIndicator(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .alpha(if (progressBarState == true) 1f else 0f),
                strokeWidth = 2.dp
            )
        }
    }

    LaunchedEffect(Unit) {
        if (hasUpdateForCheatSheetsList == null)
            viewModel.checkNewUpdateForCheatsheetsList(githubVersionName)
    }

    LaunchedEffect(hasUpdateForCheatSheetsList) {
        if (cheatSheets is Resource.Initial) {
            if (hasUpdateForCheatSheetsList == true) {
                viewModel.getCheatSheetsFromGithub()
                snackBarHostState.showSnackbar(
                    message = activity.getString(R.string.fetching_new_cheatsheets_list_msg),
                    duration = SnackbarDuration.Short
                )
            } else if (hasUpdateForCheatSheetsList == false)
                viewModel.checkNewUpdateForCheatsheetsContent(githubVersionName)
        }
    }

    LaunchedEffect(hasUpdateForCheatSheetsContent) {
        if (cheatSheets is Resource.Initial) {
            if (hasUpdateForCheatSheetsContent == true)
                viewModel.updateCheatsheetsInDatabase(githubVersionSuffix)
            else if (hasUpdateForCheatSheetsContent == false)
                viewModel.getCheatSheetFromDatabase()
        }
    }

    LaunchedEffect(updateCheatsheetsOnDBResult) {
        when (updateCheatsheetsOnDBResult) {
            is Resource.Initial -> {}
            is Resource.Loading -> {}
            is Resource.Success -> {
                viewModel.saveVersionName(githubVersionName!!)
                viewModel.getCheatSheetFromDatabase()
            }

            is Resource.Error -> {
                snackBarHostState.showSnackbar(
                    message = updateCheatsheetsOnDBResult.error?.errorMsg ?: "",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    LaunchedEffect(cheatSheets) {
        when (cheatSheets) {
            is Resource.Initial -> {}
            is Resource.Loading -> {
                viewModel.setProgressBarState(true)
            }

            is Resource.Success -> {
                viewModel.setProgressBarState(false)

                if (cheatSheets.data?.isEmpty() != false) {
                    snackBarHostState.showSnackbar(
                        message = activity.getString(R.string.no_cheatsheets_msg),
                        duration = SnackbarDuration.Long
                    )
                    return@LaunchedEffect
                }

                if (hasUpdateForCheatSheetsList == true && saveCheatsheetsListResult is Resource.Initial)
                    viewModel.saveCheatsheetsOnDB(githubVersionName!!)
            }

            is Resource.Error -> {
                viewModel.setProgressBarState(null)
                snackBarHostState.showSnackbar(
                    message = cheatSheets.error?.errorMsg.toString(),
                    duration = SnackbarDuration.Long
                )
            }
        }
    }

    LaunchedEffect(saveCheatsheetsListResult) {
        when (saveCheatsheetsListResult) {
            is Resource.Initial -> {}
            is Resource.Loading -> {}
            is Resource.Success -> {
                viewModel.saveVersionName(githubVersionName!!)
            }

            is Resource.Error -> {
                snackBarHostState.showSnackbar(
                    message = saveCheatsheetsListResult.error?.errorMsg.toString(),
                    duration = SnackbarDuration.Long
                )
            }
        }
    }

    BackHandler {
        activity.finish()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar() {
    CenterAlignedTopAppBar(
        title = { Text("Kotlin Cheat Sheet") },
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
                onClick = {}
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

@Composable
fun CheatSheetItem(
    cheatSheet: RepoFileModel,
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
                .clickable {
                    navHostController.navigate(
                        Screen.Point.appendArg(
                            cheatSheet.name,
                            cheatSheet.hasContentUpdated.toString(),
                            cheatSheet.id.toString()
                        )
                    )
                }
                .padding(8.dp)
        ) {
            val (id, title, label) = createRefs()

            Text(
                modifier = Modifier
                    .width(25.dp)
                    .height(25.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(50)
                    )
                    .constrainAs(id) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                text = cheatSheet.id.toString(),
            )

            Text(
                modifier = Modifier.constrainAs(title) {
                    start.linkTo(id.end, margin = 8.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                text = cheatSheet.name
                    .extractFileName()
                    .splitByCapitalLetters(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            if (cheatSheet.hasContentUpdated)
                Text(
                    modifier = Modifier
                        .background(
                            color = Color.Red,
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 4.dp)
                        .constrainAs(label) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                    text = stringResource(R.string.updated_label_txt),
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                    fontSize = 8.sp,
                    color = MaterialTheme.colorScheme.surface,
                    fontWeight = FontWeight.Bold
                )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(MainActivity(), rememberNavController(), "", "")
}
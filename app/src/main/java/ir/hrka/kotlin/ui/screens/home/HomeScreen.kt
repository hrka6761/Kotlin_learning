package ir.hrka.kotlin.ui.screens.home

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ir.hrka.kotlin.MainActivity
import ir.hrka.kotlin.R
import ir.hrka.kotlin.core.utilities.Constants.TAG
import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.core.utilities.Screen
import ir.hrka.kotlin.core.utilities.extractFileName
import ir.hrka.kotlin.core.utilities.splitByCapitalLetters
import ir.hrka.kotlin.domain.entities.RepoFileModel

@Composable
fun HomeScreen(
    activity: MainActivity,
    navHostController: NavHostController,
    githubVersionName: String?
) {

    val viewModel: HomeViewModel = hiltViewModel()
    val snackBarHostState = remember { SnackbarHostState() }
    val cheatSheets by viewModel.cheatSheets.collectAsState()
    val progressBarState by viewModel.progressBarState.collectAsState()
    val hasNewUpdate by viewModel.hasNewUpdate.collectAsState()
    val savePointsResult by viewModel.savePointsResult.collectAsState()


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
                val list = cheatSheets.data
                val sortedList = list?.sortedBy { it.id }

                sortedList?.let {
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
        if (hasNewUpdate == null)
            viewModel.checkNewUpdateForCheatsheetsList(githubVersionName)
    }

    LaunchedEffect(hasNewUpdate) {
        if (cheatSheets is Resource.Initial) {
            if (hasNewUpdate == true) {
                viewModel.getCheatSheetsFromGithub()
                snackBarHostState.showSnackbar(
                    message = activity.getString(R.string.fetching_new_cheatsheets_list_msg),
                    duration = SnackbarDuration.Short
                )
            } else if (hasNewUpdate == false)
                viewModel.getCheatSheetFromDatabase()

        }
    }

    LaunchedEffect(cheatSheets) {
        when (cheatSheets) {
            is Resource.Initial -> {
                viewModel.setProgressBarState(true)
            }

            is Resource.Loading -> {}
            is Resource.Success -> {
                viewModel.setProgressBarState(false)
                if (cheatSheets.data?.isEmpty() != false) {
                    snackBarHostState.showSnackbar(
                        message = activity.getString(R.string.no_cheatsheets_msg),
                        duration = SnackbarDuration.Long
                    )
                    return@LaunchedEffect
                }
                if (hasNewUpdate == true && savePointsResult is Resource.Initial)
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

    LaunchedEffect(savePointsResult) {
        when (savePointsResult) {
            is Resource.Initial -> {}
            is Resource.Loading -> {}
            is Resource.Success -> {
                viewModel.saveVersionMinor(githubVersionName!!)
            }

            is Resource.Error -> {
                snackBarHostState.showSnackbar(
                    message = savePointsResult.error?.errorMsg.toString(),
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

    var dropDownDisplayState by remember { mutableStateOf(false) }

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
                onClick = { dropDownDisplayState = !dropDownDisplayState }
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = null)
            }

            DropdownMenu(
                expanded = dropDownDisplayState,
                onDismissRequest = { dropDownDisplayState = false }
            ) {
                DropdownMenuItem(
                    text = { Text("About") },
                    onClick = {}
                )
                DropdownMenuItem(
                    text = { Text("Refresh") },
                    onClick = {}
                )
            }
        }
    )
}

@Composable
fun CheatSheetItem(cheatSheet: RepoFileModel, navHostController: NavHostController) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable {
                    navHostController.navigate(Screen.Point.appendArg(cheatSheet.name))
                }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = cheatSheet.id.toString() + ") ",
                textAlign = TextAlign.Start,
            )

            Text(
                text = cheatSheet.name
                    .extractFileName()
                    .splitByCapitalLetters(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(MainActivity(), rememberNavController(), "")
}
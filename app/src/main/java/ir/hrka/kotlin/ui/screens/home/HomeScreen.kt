package ir.hrka.kotlin.ui.screens.home

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ir.hrka.kotlin.MainActivity
import ir.hrka.kotlin.core.Constants.TAG

@Composable
fun HomeScreen(
    activity: MainActivity,
    navHostController: NavHostController,
    githubVersionName: String?,
    githubVersionSuffix: String?
) {

}


@Composable
fun HomeScreenPreview() {
    HomeScreen(MainActivity(), rememberNavController(), "", "")
}
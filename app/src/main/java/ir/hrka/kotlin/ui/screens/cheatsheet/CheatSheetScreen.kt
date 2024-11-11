package ir.hrka.kotlin.ui.screens.cheatsheet

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ir.hrka.kotlin.MainActivity
import ir.hrka.kotlin.core.utilities.Constants.TAG

@Composable
fun CheatSheetScreen(activity: MainActivity, navHostController: NavHostController, cheatSheetFileName: String) {

}


@Preview(showBackground = true)
@Composable
fun CheatSheetScreenPreview() {
    CheatSheetScreen(MainActivity(), rememberNavController(), "")
}
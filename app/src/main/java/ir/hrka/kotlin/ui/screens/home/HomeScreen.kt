package ir.hrka.kotlin.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ir.hrka.kotlin.MainActivity

@Composable
fun HomeScreen(activity: MainActivity, navHostController: NavHostController) {

}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(MainActivity(), rememberNavController())
}
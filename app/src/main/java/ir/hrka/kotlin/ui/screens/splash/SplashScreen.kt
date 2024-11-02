package ir.hrka.kotlin.ui.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ir.hrka.kotlin.MainActivity

@Composable
fun SplashScreen(activity: MainActivity, navHostController: NavHostController) {

}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(MainActivity(), rememberNavController())
}
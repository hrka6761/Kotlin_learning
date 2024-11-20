package ir.hrka.kotlin.ui.screens.about

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ir.hrka.kotlin.MainActivity

@Composable
fun ProfileScreen(activity: MainActivity, navHostController: NavHostController) {

}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(MainActivity(), rememberNavController())
}
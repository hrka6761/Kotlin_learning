package ir.hrka.kotlin.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ir.hrka.kotlin.MainActivity
import ir.hrka.kotlin.R

@Composable
fun SplashScreen(activity: MainActivity, navHostController: NavHostController) {

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (logo, title, authorImg, authorName, progressBar) = createRefs()

        Image(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .constrainAs(logo) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
        )

        Text(
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(logo.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            text = "Kotlin Cheat Sheet"
        )

        Text(
            modifier = Modifier
                .constrainAs(authorName) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                },
            text = "Powered by HAMIDREZA KARAMI",
            fontSize = 8.sp
        )

        CircularProgressIndicator(
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .constrainAs(progressBar) {
                    bottom.linkTo(authorName.top, margin = 2.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            strokeWidth = 1.dp
        )

        Image(
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
                .constrainAs(authorImg) {
                    bottom.linkTo(authorName.top, margin = 7.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            painter = painterResource(R.drawable.author),
            contentDescription = null,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(MainActivity(), rememberNavController())
}
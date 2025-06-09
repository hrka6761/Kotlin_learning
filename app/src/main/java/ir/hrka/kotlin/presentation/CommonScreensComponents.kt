package ir.hrka.kotlin.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.hrka.kotlin.R
import ir.hrka.kotlin.core.utilities.ExecutionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier,
    title: String,
    navigationClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                modifier = modifier.padding(end = 16.dp),
                text = title,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            navigationClick?.let {
                IconButton(
                    onClick = it
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        },
        actions = actions
    )
}

@Composable
fun Loading(
    modifier: Modifier = Modifier,
    executionState: ExecutionState
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = modifier
                .width(50.dp)
                .height(50.dp)
                .alpha(if (executionState == ExecutionState.Loading) 1f else 0f)
                .align(Alignment.Center),
            strokeWidth = 2.dp
        )
    }
}

@Composable
fun Failed(
    modifier: Modifier = Modifier,
    failedState: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .alpha(if (failedState) 1f else 0f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = modifier
                .width(60.dp)
                .height(60.dp),
            painter = painterResource(R.drawable.error),
            contentDescription = null
        )

        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            text = stringResource(R.string.failed_to_fetch_the_data),
            textAlign = TextAlign.Center
        )
    }
}
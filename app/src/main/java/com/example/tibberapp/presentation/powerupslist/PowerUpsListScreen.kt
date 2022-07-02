package com.example.tibberapp.presentation.powerupslist


import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tibberapp.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.tibberapp.domain.model.AssignmentData
import com.example.tibberapp.presentation.util.Screen
import com.example.tibberapp.ui.theme.Gray400
import com.example.tibberapp.ui.theme.Gray600
import com.example.tibberapp.ui.theme.TibberBlue
import com.example.tibberapp.ui.theme.TibberGray
import com.example.tibberapp.util.Constants.POWER_UP_DATA_KEY
import com.skydoves.landscapist.glide.GlideImage

@ExperimentalFoundationApi
@Composable
fun PowerUpsListScreen(
    navController: NavController,
    viewModel: PowerUpsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.power_ups_title)) }
            )
        },
        scaffoldState = rememberScaffoldState()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(TibberGray)
        ) {
            PowerUpsList(
                navController = navController,
                viewModel = viewModel,
                LocalContext.current
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun PowerUpsList(
    navController: NavController,
    viewModel: PowerUpsViewModel,
    context: Context
) {
    val powerUpsList by remember { viewModel.powerUpsList }
    val isLoading by remember { viewModel.isLoading }
    val loadError by remember { viewModel.loadError }

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        val groupedPowerUps = powerUpsList.groupBy { it.connected }
        groupedPowerUps.forEach { (connected, powerUps) ->
            stickyHeader {
                Text(
                    text = if (connected) stringResource(id = R.string.active_power_ups_label) else stringResource(
                        id = R.string.available_power_ups_label
                    ),
                    color = Gray400,
                    modifier = Modifier
                        .background(TibberGray)
                        .padding(5.dp)
                        .fillMaxWidth()
                )
            }
            items(powerUps) { item ->
                PowerUpItem(assignmentData = item, navController)
            }
        }

    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = TibberBlue, modifier = Modifier.align(TopCenter))
        }
        if (loadError.asString().isNotEmpty()) {
            RetryView(error = loadError.asString(context)) {
                viewModel.loadPowerUps()
            }
        }
    }
}


@Composable
fun PowerUpItem(
    assignmentData: AssignmentData,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(
                Color.White
            )
            .clickable {
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = POWER_UP_DATA_KEY,
                    value = assignmentData
                )
                navController.navigate(Screen.PowerUpDetailsScreen.route)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = CenterVertically
        ) {
            Column {
                GlideImage(
                    imageModel = assignmentData.imageUrl,
                    contentDescription = assignmentData.title,
                    modifier = Modifier
                        .size(60.dp),
                    requestBuilder = Glide
                        .with(LocalView.current)
                        .asBitmap()
                        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                        .transition(BitmapTransitionOptions.withCrossFade()),
                    loading = {
                        ConstraintLayout(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            val indicator = createRef()
                            CircularProgressIndicator(
                                modifier = Modifier.constrainAs(indicator) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                            )
                        }
                    },
                    success = { glideState ->
                        glideState.imageBitmap?.let {
                            Image(
                                bitmap = it,
                                contentDescription = assignmentData.title,
                                modifier = Modifier
                                    .size(60.dp)
                            )
                        }
                    }
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .padding(end = 15.dp)
                    .weight(1f)
            ) {
                Text(
                    text = assignmentData.title,
                    style = MaterialTheme.typography.h6,
                    color = Gray600,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = assignmentData.description,
                    style = MaterialTheme.typography.body1,
                    color = Gray400,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start
                )
            }
            Column(modifier = Modifier.padding(end = 16.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "Arrow right"
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun RetryView(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(
            text = error, color = Color.Red, fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                onRetry()
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(horizontal = 55.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(backgroundColor = TibberBlue)
        ) {
            Text(text = stringResource(id = R.string.retry_label))
        }
    }
}


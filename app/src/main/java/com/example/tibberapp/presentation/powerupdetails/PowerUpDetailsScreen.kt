package com.example.tibberapp.presentation.powerupdetails

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.tibberapp.R
import com.example.tibberapp.domain.model.AssignmentData
import com.example.tibberapp.ui.theme.*
import com.example.tibberapp.util.Constants.POWER_UP_DATA_KEY
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun PowerUpDetailsScreen(
    navController: NavController
) {
    val powerUp =
        navController.previousBackStackEntry?.savedStateHandle?.get<AssignmentData>(
            POWER_UP_DATA_KEY
        ) ?: AssignmentData()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = powerUp.title) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back Btn")
                    }
                }
            )
        },
        scaffoldState = rememberScaffoldState()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(TibberGray)
                .verticalScroll(rememberScrollState())
        ) {
            PowerUpHeader(powerUp.title, powerUp.description, powerUp.imageUrl)
            PowerUpActionButtons(powerUp.connected)
            PowerUpMoreData(powerUp.title, powerUp.longDescription)
        }
    }
}

@Composable
fun PowerUpHeader(
    title: String,
    description: String,
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                GlideImage(
                    imageModel = imageUrl,
                    contentDescription = title,
                    modifier = Modifier
                        .size(80.dp),
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
                                contentDescription = title,
                                modifier = Modifier
                                    .size(80.dp)
                            )
                        }
                    }
                )
            }
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    color = Gray600,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.body1,
                    color = Gray400,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
fun PowerUpActionButtons(
    connected: Boolean
) {
    Spacer(modifier = Modifier.height(24.dp))
    if (connected)
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 35.dp),
            border = BorderStroke(1.dp, TibberRed),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(backgroundColor = TibberGray)
        ) {
            Text(
                text = stringResource(id = R.string.disconnect_to_tibber_label),
                color = TibberRed
            )
        }
    else
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 35.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(backgroundColor = TibberBlue)
        ) {
            Text(text = stringResource(id = R.string.connect_to_tibber_label))
        }
    Spacer(modifier = Modifier.height(24.dp))
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 35.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
    ) {
        Text(text = stringResource(id = R.string.buy_at_tibber_label), color = Gray900)
    }
    Spacer(modifier = Modifier.height(24.dp))

}

@Composable
fun PowerUpMoreData(
    title: String,
    longDescription: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.power_up_more_data_label, title),
                style = MaterialTheme.typography.h6,
                color = Gray600,
                textAlign = TextAlign.Start
            )
            Text(
                text = longDescription,
                style = MaterialTheme.typography.body1,
                color = Gray400,
                textAlign = TextAlign.Start
            )
        }
    }
}

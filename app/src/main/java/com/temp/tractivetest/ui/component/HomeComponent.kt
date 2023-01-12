package com.temp.tractivetest.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.temp.tractivetest.R
import com.temp.tractivetest.data.CallResult
import com.temp.tractivetest.data.PetCalls
import com.temp.tractivetest.ui.SemanticsUtil
import com.temp.tractivetest.ui.StringId
import com.temp.tractivetest.ui.theme.Proximity
import com.temp.tractivetest.ui.theme.Teal200
import com.temp.tractivetest.ui.theme.Teal200ST
import com.temp.tractivetest.ui.theme.TextError
import com.temp.tractivetest.viewmodel.HomeViewModel

/**
 * entry point for Home view UI page
 * @param navController navigation controller to enable forward navigation
 */
@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeComponent(navController: NavHostController) {
    val viewModel: HomeViewModel = hiltViewModel()
    val pet = viewModel.pet
    val callResult = viewModel.position.collectAsStateWithLifecycle(CallResult.idle())
    HomePage(navController, callResult, pet) {
        viewModel.refresh()
    }
}

/**
 * Home view
 * @param navController navigation controller to enable forward navigation
 * @param callResult result of a call for data
 * @param refresh handle to call data refresh
 */
@Composable
private fun HomePage(navController: NavHostController, callResult: State<CallResult<Int>>, pet: PetCalls.Pet, refresh: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        callResult.value.let {
            when (it.status) {
                CallResult.Status.SUCCESS -> {
                    ProximityView(distance = it.data ?: 0, pet = pet)
                }
                CallResult.Status.LOADING, CallResult.Status.IDLE -> {
                    LoadingView()
                }
                CallResult.Status.ERROR -> {
                    Text(
                        text = stringResource(R.string.err_pet_lost),
                        style = TextStyle(color = TextError)
                    )
                }
            }
        }

        Text(
            modifier = Modifier.padding(10.dp),
            text = stringResource(R.string.lbl_explanation)
        )
    }
}

@Composable
private fun ProximityView(distance: Int, pet: PetCalls.Pet) {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)) {
        val (image) = createRefs()
        Image(painter = painterResource(id = pet.image),
            contentDescription = stringResource(id = R.string.desc_img_home),
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.imageSize))
                .clip(CircleShape)
                .background(Teal200)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
        Box(modifier = Modifier
            .border(distance.dp, Teal200ST, CircleShape)
            .background(Color.Transparent)
            .constrainAs(createRef()) {
                top.linkTo(image.top, -distance.dp)
                bottom.linkTo(image.bottom, -distance.dp)
                start.linkTo(image.start, -distance.dp)
                end.linkTo(image.end, -distance.dp)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            })
        val dividedDistance = distance.div(5)
        for (i: Int in 1..dividedDistance) {
            Box(modifier = Modifier
                .semantics { StringId = SemanticsUtil.getProximitySemantics(i) }
                .clip(CircleShape)
                .border(2.dp, Proximity, CircleShape)
                .background(Color.Transparent)
                .constrainAs(createRef()) {
                    top.linkTo(image.top, (-10 * i).dp)
                    bottom.linkTo(image.bottom, (-10 * i).dp)
                    start.linkTo(image.start, (-10 * i).dp)
                    end.linkTo(image.end, (-10 * i).dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                })
        }
    }
}

@Composable
private fun LoadingView() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)){
        CircularProgressIndicator(modifier = Modifier
            .size(dimensionResource(id = R.dimen.imageSize))
            .align(Alignment.Center)
            .clip(CircleShape)
            .background(Teal200))
    }

}

/**
 * Preview of the entire page
 */
@Preview(showBackground = true)
@Composable
private fun PagePreview() {
    val state = produceState(initialValue = CallResult.success(30)) {
        value = CallResult.success(30)
    }
    HomePage(rememberNavController(), state) {}
}

/**
 * Preview of loading component
 */
@Preview(showBackground = true)
@Composable
private fun LoadPreview() {
    val state = produceState(initialValue = CallResult.loading(1)) {
        value = CallResult.loading()
    }
    HomePage(rememberNavController(), state) {}
}

/**
 * Preview of error component
 */
@Preview(showBackground = true)
@Composable
private fun ErrorPreview() {
    val state = produceState(initialValue = CallResult.error<Int>()) {
        value = CallResult.error()
    }
    HomePage(rememberNavController(), state) {}
}
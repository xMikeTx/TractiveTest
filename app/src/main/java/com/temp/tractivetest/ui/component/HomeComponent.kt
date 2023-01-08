package com.temp.tractivetest.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.temp.tractivetest.ui.theme.Proximity
import com.temp.tractivetest.ui.theme.Teal200
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
    val callResult = viewModel.position.collectAsStateWithLifecycle(CallResult.idle())
    HomePage(navController, callResult) {
        viewModel.refresh()
    }
}

/**
 * entry point for Hit view UI page
 * @param navController navigation controller to enable forward navigation
 * @param callResult result of a call for data
 * @param refresh handle to call data refresh
 */
@Composable
private fun HomePage(navController: NavHostController, callResult: State<CallResult<Int>>, refresh: () -> Unit) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
        val (image) = createRefs()
        Image(painter = painterResource(id = R.drawable.ic_pet),
            contentDescription = stringResource(id = R.string.desc_img_home),
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .background(Teal200)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
        callResult.value.let {
            when (it.status) {
                CallResult.Status.SUCCESS -> {
                    val position = it.data?.div(5) ?: 0
                    for (i: Int in 1..position) {
                        Box(modifier = Modifier
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
                CallResult.Status.LOADING, CallResult.Status.IDLE -> {
                    LoadingItem()
                }
                CallResult.Status.ERROR -> {
                    Text(
                        text = stringResource(R.string.err_pet_lost),
                        modifier = Modifier.fillMaxWidth(),
                        style = TextStyle(color = TextError)
                    )
                }
            }
        }
    }
}

/**
 * Component that composes a proximity circle
 */
@Composable
private fun ProximityIndicator(position: Int) {

}

/**
 * Component that shows a skeleton loading
 */
@Composable
private fun LoadingItem() {

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
    LoadingItem()
}
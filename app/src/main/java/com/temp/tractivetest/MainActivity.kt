package com.temp.tractivetest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.temp.tractivetest.ui.Navigation
import com.temp.tractivetest.ui.component.HomeComponent
import com.temp.tractivetest.ui.theme.TractiveTestTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TractiveTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AppBar(navController)
                        MainScreen(navController)
                    }
                }
            }
        }
    }
}

/**
 * AppBar for the top of the application
 * @param navController used to infer where we are in the app and change AppBar accordingly
 */
@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AppBar(navController: NavHostController) {
    val currentRoute = navController
        .currentBackStackEntryFlow
        .collectAsStateWithLifecycle(initialValue = navController.currentBackStackEntry)

    val currentNav = Navigation.fromRoute(currentRoute.value?.destination?.route)
    val showBackButton = when (currentNav) {
        Navigation.Home -> false
        else -> true
    }
    TopAppBar(
        title = {
            Text(stringResource(id = currentNav.title))
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(
                    onClick = { navController.popBackStack() },
                ) { Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(R.string.desc_img_back)) }
            } else {
                IconButton(
                    onClick = { navController.popBackStack(Navigation.Home.route, false) }
                ) { Icon(imageVector = Icons.Default.Home, contentDescription = stringResource(R.string.desc_img_home)) }
            }
        }
    )
}

/**
 * MainScreen that leads to the correct page using navigation
 * @param navController used to compose the correct page
 */
@Composable
fun MainScreen(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Navigation.Home.route,
    ) {
        composable(Navigation.Home.route, Navigation.Home.getNavArguments()) {
            HomeComponent(navController = navController)
        }
    }
}
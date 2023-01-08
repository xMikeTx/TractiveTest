package com.temp.tractivetest.ui

import androidx.annotation.StringRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import com.temp.tractivetest.R

/**
 * Navigation class which encompasses all navigation destinations
 * Routes follow Base/{Args} format where Base represents a unique path
 * and args represent arguments for that route
 */
sealed class Navigation(val route: String, @StringRes val title : Int) {
    companion object {
        const val NAV_ID = "id"

        /**
         * utility method to get route object from route string
         */
        fun fromRoute(route: String?) =
            when (route?.substringBefore("/")) {
                Home.route.substringBefore("/") -> Home
                else -> Home
            }
    }

    /**
     * list of navigation arguments
     */
    abstract fun getNavArguments(): List<NamedNavArgument>

    /**
     * Home destination where app starts from
     */
    object Home : Navigation("HOME", R.string.title_home) {
        fun NavController.navigateToHome() {
            navigate("HOME")
        }

        override fun getNavArguments(): List<NamedNavArgument> = listOf()
    }
}
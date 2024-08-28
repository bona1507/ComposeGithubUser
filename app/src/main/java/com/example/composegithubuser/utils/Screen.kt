package com.example.composegithubuser.utils

sealed class Screen(val route: String){
    data object Home : Screen("home")
    data object Favorite : Screen("favorite")
    data object About : Screen("profile")
    data object DetailUser : Screen("home/{username}") {
        fun createRoute(username: String) = "home/$username"
    }
}

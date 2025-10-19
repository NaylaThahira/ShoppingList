package com.example.shoppinglist.screen

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Shopping List")
    object Profile : Screen("profile", "Profile")
    object Setting : Screen("setting", "Settings")
    object Detail : Screen("detail/{itemName}", "Detail")
    object About : Screen("about_app", "About App")
}
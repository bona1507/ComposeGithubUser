package com.example.composegithubuser

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.composegithubuser.utils.Navigation
import com.example.composegithubuser.utils.Screen
import com.example.composegithubuser.ui.detail.DetailPage
import com.example.composegithubuser.ui.favorite.FavoritePage
import com.example.composegithubuser.ui.home.HomePage
import com.example.composegithubuser.ui.profile.ProfilePage
import com.example.composegithubuser.ui.theme.MainAppTheme

@Composable
fun MainApp(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            when (currentRoute) {
                Screen.Home.route -> MyTopBar(title = stringResource(id = R.string.app_name))
                Screen.Favorite.route -> MyTopBar(title = stringResource(id = R.string.favorite))
                Screen.About.route -> MyTopBar(title = stringResource(id = R.string.profile))
            }
        },
        bottomBar = {
            if (currentRoute != Screen.DetailUser.route) {
                BottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            listOf(Screen.Home, Screen.Favorite, Screen.About).forEach { screen ->
                composable(screen.route) {

                    val navigateDetail: (String) -> Unit = { username ->
                        navController.navigate(Screen.DetailUser.createRoute(username))
                    }

                    when (screen) {
                        Screen.Home -> HomePage(navigateToDetail = navigateDetail)
                        Screen.Favorite -> FavoritePage(navigateToDetail = navigateDetail)
                        Screen.About -> ProfilePage()
                        Screen.DetailUser -> navController.navigateUp()
                    }
                }
            }
            composable(
                route = Screen.DetailUser.route,
                arguments = listOf(navArgument("username") { type = NavType.StringType })
            ) {
                DetailPage(username = it.arguments?.getString("username") ?: "") {
                    navController.navigateUp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    title: String
) {
    TopAppBar(
        title = {
            Text(text = title)
        },
    )
}

@Composable
private fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            Navigation(
                title = stringResource(id = R.string.home),
                icon = Icons.Default.Home,
                screen = Screen.Home,
            ),
            Navigation(
                title = stringResource(R.string.favorite),
                icon = Icons.Default.Favorite,
                screen = Screen.Favorite
            ),
            Navigation(
                title = stringResource(R.string.profile),
                icon = Icons.Default.AccountCircle,
                screen = Screen.About
            ),
        )
        navigationItems.map { item ->
            NavigationBarItem(
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon ,
                        contentDescription = item.title
                    )
                },
                label = { Text(text = item.title)}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GithubUserAppPreview() {
    MainAppTheme {
        MainApp()
    }
}
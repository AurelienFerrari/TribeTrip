package org.example.project.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import org.example.project.ui.screens.TripHomeScreen
import org.example.project.ui.screens.TripProfileScreen
import org.example.project.ui.screens.TripSearchScreen

sealed class Screen {
    @Serializable
    data object Home : Screen()

    @Serializable
    data object Search : Screen()

    @Serializable
    data object Profil : Screen()
}

data class ElementNavigationInferieure(
    val route: Screen,
    val icone: ImageVector,
    val libelle: String
)

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController: NavHostController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destinationActuelle = navBackStackEntry?.destination

    val elementsNavigation = listOf(
        ElementNavigationInferieure(Screen.Home, Icons.Default.Home, "Accueil"),
        ElementNavigationInferieure(Screen.Search, Icons.Default.Search, "Recherche"),
        ElementNavigationInferieure(Screen.Profil, Icons.Default.Person, "Profil")
    )

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                elementsNavigation.forEach { element ->
                    NavigationBarItem(
                        icon = { Icon(element.icone, contentDescription = element.libelle) },
                        label = { Text(element.libelle) },
                        selected = destinationActuelle?.hasRoute(element.route::class) == true,
                        onClick = {
                            navController.navigate(element.route) {
                                popUpTo(Screen.Home) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<Screen.Home> {
                TripHomeScreen()
            }
            composable<Screen.Search> {
                TripSearchScreen()
            }
            composable<Screen.Profil> {
                TripProfileScreen()
            }
        }
    }
}

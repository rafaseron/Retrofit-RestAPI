package br.com.alura.anyflix.navigation.navHost

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import br.com.alura.anyflix.navigation.cepScreenNavigation
import br.com.alura.anyflix.navigation.homeRoute
import br.com.alura.anyflix.navigation.homeScreen
import br.com.alura.anyflix.navigation.movieDetailsRouteFullpath
import br.com.alura.anyflix.navigation.movieDetailsScreen
import br.com.alura.anyflix.navigation.myListRoute
import br.com.alura.anyflix.navigation.myListScreen
import br.com.alura.anyflix.navigation.navigateToHome
import br.com.alura.anyflix.navigation.navigateToMovieDetails
import br.com.alura.anyflix.navigation.navigateToMyList
import br.com.alura.anyflix.ui.components.BottomAppBarItem

@Composable
fun AnyflixNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = homeRoute
    ) {
        homeScreen(
            onNavigateToMovieDetails = { movie ->
                navController.navigateToMovieDetails(movie.id)
            },
        )
        movieDetailsScreen(
            onNavigateToMovieDetails = { movie ->
                navController.navigateToMovieDetails(
                    movie.id,
                    navOptions {
                        popUpTo(movieDetailsRouteFullpath) {
                            inclusive = true
                        }
                    }
                )
            },
            onPopBackStack = {
                navController.popBackStack()
            }
        )
        myListScreen(
            onNavigateToHome = {
                navController.navigateToHome(navOptions {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                })
            },
            onNavigateToMovieDetails = {
                navController.navigateToMovieDetails(it.id)
            }
        )

        cepScreenNavigation()
    }
}

fun NavController.navigateToBottomAppBarItem(
    item: BottomAppBarItem,
) {
    when (item) {
        BottomAppBarItem.Home -> {
            navigateToHome(
                navOptions {
                    launchSingleTop = true
                    popUpTo(homeRoute)
                }
            )
        }
        BottomAppBarItem.MyList -> {
            navigateToMyList(
                navOptions {
                    launchSingleTop = true
                    popUpTo(myListRoute)
                }
            )
        }

        BottomAppBarItem.Cep -> {
            //TODO navigateToCep
        }
    }
}

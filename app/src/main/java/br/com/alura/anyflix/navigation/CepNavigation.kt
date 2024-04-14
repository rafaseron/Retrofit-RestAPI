package br.com.alura.anyflix.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.alura.anyflix.ui.screens.CepScreen
import br.com.alura.anyflix.ui.viewmodels.CepViewModel

private const val cepRoute = "cepRoute"
fun NavGraphBuilder.cepScreenNavigation(){

    composable(route = cepRoute){
        val viewModel = hiltViewModel<CepViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        CepScreen(uiState = uiState, viewModel = viewModel)
    }
}

fun NavController.navigateToCepScreen(){
    navigate(route = cepRoute){
        launchSingleTop = true
        popUpTo(route = cepRoute)
    }
}
package br.com.alura.anyflix.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.alura.anyflix.ui.screens.CepScreen
import br.com.alura.anyflix.ui.viewmodels.CepViewModel
import kotlinx.coroutines.launch

internal const val cepRoute = "cepRoute"
fun NavGraphBuilder.cepScreenNavigation(){

    composable(route = cepRoute){
        val viewModel = hiltViewModel<CepViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val scope = rememberCoroutineScope()

        CepScreen(uiState = uiState, viewModel = viewModel,
            onButtonClick = {
                scope.launch { viewModel.onButtonClick() }
            })
    }
}

fun NavController.navigateToCepScreen(){
    navigate(route = cepRoute){
        launchSingleTop = true
        popUpTo(route = cepRoute)
    }
}
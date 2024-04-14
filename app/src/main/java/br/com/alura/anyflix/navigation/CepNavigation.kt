package br.com.alura.anyflix.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.alura.anyflix.ui.screens.CepScreen
import br.com.alura.anyflix.ui.viewmodels.CepViewModel

private const val cepRoute = "cepRoute"
fun NavGraphBuilder.cepScreenNavigation(){

    composable(route = cepRoute){
        val viewModel = hiltViewModel<CepViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        CepScreen()

        //TODO rodar o Composable aqui dentro
    }

}
package br.com.alura.anyflix.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.alura.anyflix.data.repository.MovieRepository
import br.com.alura.anyflix.ui.viewmodels.CepUiState
import br.com.alura.anyflix.ui.viewmodels.CepViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun cepScreen(uiState: CepUiState, viewModel: CepViewModel) {
    Column(modifier = Modifier.fillMaxSize(1f)) {

        Text(
            text = "Cadastro de Endereço",
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
            fontWeight = FontWeight(400), fontSize = 20.sp
        )

        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = uiState.cep, onValueChange = {viewModel.cepValueChange(it)},
                modifier = Modifier.fillMaxWidth(1f), label = { Text(text = "Cep")})

            Spacer(modifier = Modifier)

            OutlinedTextField(value = uiState.logradouro, onValueChange = {viewModel.logradouroValueChange(it)},
                modifier = Modifier.fillMaxWidth(1f), label = { Text(text = "Logradouro")})

            Spacer(modifier = Modifier)

            OutlinedTextField(value = uiState.numero, onValueChange = {viewModel.numeroValueChange(it)},
                modifier = Modifier.fillMaxWidth(1f), label = { Text(text = "Numero")})

            Spacer(modifier = Modifier)

            OutlinedTextField(value = uiState.bairro, onValueChange = {viewModel.bairroValueChange(it)},
                modifier = Modifier.fillMaxWidth(1f), label = { Text(text = "Bairro")})

            Spacer(modifier = Modifier)

            OutlinedTextField(value = uiState.cidade, onValueChange = {viewModel.cidadeValueChange(it)},
                modifier = Modifier.fillMaxWidth(1f), label = { Text(text = "Cidade")})

            Spacer(modifier = Modifier)

            OutlinedTextField(value = uiState.estado, onValueChange = {viewModel.estadoValueChange(it)},
                modifier = Modifier.fillMaxWidth(1f), label = { Text(text = "Estado")})

            Spacer(modifier = Modifier)

            OutlinedTextField(value = uiState.complemento, onValueChange = {viewModel.complementoValueChange(it)},
                modifier = Modifier.fillMaxWidth(1f), label = { Text(text = "Complemento")})

            Spacer(modifier = Modifier)
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun cepPreview() {
    //cepScreen(uiState = CepUiState("", "", "", "", "", "", ""))
}
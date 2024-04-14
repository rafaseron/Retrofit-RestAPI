package br.com.alura.anyflix.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alura.anyflix.data.repository.CepRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CepUiState(val cep: String = "", val rua: String = "", val numero: String = "", val bairro: String = "", val cidade: String = "",
                      val estado: String = "", val complemento: String = "")

@HiltViewModel
class CepViewModel @Inject constructor(private val service: CepRepository): ViewModel(){

    private val _uiState = MutableStateFlow(CepUiState())
    val uiState = _uiState.asStateFlow()


    suspend fun onButtonClick(){
        viewModelScope.launch {
            service.getAddressFromCep(uiState.value.cep).collect{
                cepModel ->
                _uiState.value = _uiState.value.copy(rua = cepModel.rua, numero = cepModel.numero, bairro = cepModel.bairro, cidade = cepModel.cidade, estado = cepModel.estado)
            }
        }
    }

    fun cepValueChange(newValue: String){
        _uiState.value = _uiState.value.copy(cep = newValue)
    }

    fun logradouroValueChange(newValue: String){
        _uiState.value = _uiState.value.copy(rua = newValue)
    }

    fun numeroValueChange(newValue: String){
        _uiState.value = _uiState.value.copy(numero = newValue)
    }

    fun bairroValueChange(newValue: String){
        _uiState.value = _uiState.value.copy(bairro = newValue)
    }

    fun cidadeValueChange(newValue: String){
        _uiState.value = _uiState.value.copy(cidade = newValue)
    }

    fun estadoValueChange(newValue: String){
        _uiState.value = _uiState.value.copy(estado = newValue)
    }

    fun complementoValueChange(newValue: String){
        _uiState.value = _uiState.value.copy(complemento = newValue)
    }

}
package br.com.alura.anyflix.data.repository

import android.util.Log
import br.com.alura.anyflix.data.network.CepResponse
import br.com.alura.anyflix.data.network.CepService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


data class Cep(val cep: String, val rua: String, val numero: String, val bairro: String, val cidade: String, val estado: String)
class CepRepository @Inject constructor(private val service: CepService){

    suspend fun getAddressFromCep(cep: String): Flow<Cep> = flow{
        try {
            val receivedCep =  service.getCepFromCepId(cep)
            emit(receivedCep.toCep())
        }catch (e: Exception){
            Log.e("CepRepository", "getAddressFromCep Error -> ${e.message}")
        }
    }

}

fun CepResponse.toCep(): Cep{
    return Cep(cep = cep,
        rua = logradouro,
        numero = complemento,
        bairro = bairro,
        cidade = localidade,
        estado = uf
    )
}
package br.com.alura.anyflix.data.network

import retrofit2.http.GET
import retrofit2.http.Path

data class CepResponse(val cep: String, val logradouro: String, val complemento: String, val bairro: String, val localidade: String,
    val uf: String, val ibge: String, val gia: String, val ddd: String, val siafi: String)


interface CepService{
    @GET("{cepId}/json/")
    suspend fun getCepFromCepId(@Path("cepId") cepId: String): CepResponse

}
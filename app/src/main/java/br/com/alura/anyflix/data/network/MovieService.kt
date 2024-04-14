package br.com.alura.anyflix.data.network

import br.com.alura.anyflix.data.model.Movie
import retrofit2.http.GET
import retrofit2.http.Path


data class MovieResponse(val id: String, val title: String, val image: String, val year: Int, val plot: String, val inMyList: Boolean)

fun MovieResponse.toMovie(): Movie {
    return Movie(id = id, title = title, year = year, plot = plot, image = image, inMyList = inMyList)
}

interface MovieService {
    @GET("movies")
    suspend fun getAll(): List<MovieResponse>

    @GET("movies/myList")
    suspend fun getMyList(): List<MovieResponse>

    @GET("movies/{movieId}")
    suspend fun getMovieFromId(@Path("movieId") movieId: String): MovieResponse

}
package br.com.alura.anyflix.data.repository

import android.util.Log
import br.com.alura.anyflix.data.model.Movie
import br.com.alura.anyflix.data.model.toMovieEntity
import br.com.alura.anyflix.data.network.MovieResponse
import br.com.alura.anyflix.data.network.MovieService
import br.com.alura.anyflix.data.network.toMovie
import br.com.alura.anyflix.data.room.database.AnyflixDatabase
import br.com.alura.anyflix.data.room.entities.MovieEntity
import br.com.alura.anyflix.data.room.entities.toMovie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class MovieRepository @Inject constructor(database: AnyflixDatabase, private val service: MovieService) {
    private val movieDao = database.movieDao()


    //CAMADA ONLINE
    private suspend fun getMoviesFromService(): Flow<List<Movie>> = flow{
        try {
            val listMovie = mutableListOf<Movie>()
            for (movieResponse in service.getAll()) {
                listMovie.add(movieResponse.toMovie())
            }
            emit(listMovie)
        } catch (e: Exception){
            Log.e("MovieRepository", "Erro no getMoviesFromService -> ${e.message}")
        }
    }

    private suspend fun getMyListFromService(): Flow<List<Movie>> = flow{
        try {
            val listMovie = mutableListOf<Movie>()
            for (movieResponse in service.getMyList()) {
                listMovie.add(movieResponse.toMovie())
            }
            emit(listMovie)
        } catch (e: Exception){
            Log.e("MovieRepository", "Erro no getMyListFromService -> ${e.message}")
        }
    }

    private suspend fun getMovieByIdFromService(movieId: String): Flow<MovieResponse> = flow{
        try {
            emit(service.getMovieFromId(movieId))
        }catch (e: Exception){
            Log.e("MovieRepository", "getMovieByIdFromService Error -> ${e.message}")
        }
    }

    //CAMADA MISTA - Service + Dao
    suspend fun getAllSections(): Flow<Map<String, List<Movie>>>{

        //recebe os filmes do Service e Salva no Dao
        try {
            CoroutineScope(coroutineContext).launch {
                getMoviesFromService().collect { listMovie ->
                    for (movie in listMovie) {
                        movieDao.save(movie.toMovieEntity())
                    }
                }
            }
        } catch (e: Exception){
            Log.e("MovieRepository", "Erro no getAllSections -> ${e.message}")
        }

        //retorna um Flow para o ViewModel -> para que ele atualize a Ui
        return movieDao.findAll().map {
                listMovieEntity ->
            val listMovie = listMovieEntity.map { it.toMovie() }
            if (listMovie.isEmpty()) {
                emptyMap()
            } else {
                createSections(listMovie)
            }
        }
    }

    suspend fun myMovieList(): Flow<List<Movie>>{
        //recebe do Service e Salva no Dao
        try {
            CoroutineScope(coroutineContext).launch {
                getMyListFromService().collect {
                        listMovie ->
                    for (movie in listMovie){
                        movieDao.addToMyList(movie.id)
                    }
                }
            }
        } catch (e: Exception){
            Log.e("MovieRepository", "myMovieList requisicao -> ${e.message}")
        }

        //retorna o conteudo do Dao - antes, tem que transformar Flow<List<MovieEntity>> para Flow<List<Movie>>
        return movieDao.myList().map {
            listMovieEntity ->
            listMovieEntity.map {
                movieEntity ->
                movieEntity.toMovie()
            }
        }
    }

    suspend fun findMovieById(id: String): Flow<Movie>{
        //recebe do Service
        try {
            val receivedMovieFromService = getMovieByIdFromService(id) //aqui recebemos um Flow<MovieResponse>. Precisaremos transformar para salvar no Dao e/ou exibir na tela
            receivedMovieFromService.collect{ //tem que usar .collect{ }, first() cancela o emit() recebido e ai o flow é abortado
                movieResponse ->
                movieDao.save(movieResponse.toMovie().toMovieEntity())  //conversao de type para salvar no Dao
            }
        }catch (e: Exception){
            Log.e("MovieRepository", "findMovieById Error -> ${e.message}")
        }

        //retorna do Dao para o ViewModel - antes, converte de MovieEntity para Movie
        return movieDao.findMovieById(id)
            .map {
                it.toMovie()
            }
    }

    //CAMADA DE REGRA DE NEGOCIO DA UI
    private fun createSections(movies: List<Movie>) = mapOf(
        "Em alta" to movies.shuffled().take(7),
        "Novidades" to movies.shuffled().take(7),
        "Continue assistindo" to movies.shuffled().take(7)
    )

    //CAMADA OFFLINE
    suspend fun insertMovie(movie: MovieEntity){
        return movieDao.save(movie)
    }

    fun getAllMovies(): Flow<List<MovieEntity>>{
        return movieDao.findAll()
    }

    suspend fun removeFromMyList(id: String){
        return movieDao.removeFromMyList(id)
    }

    suspend fun addToMyList(id: String){
        return movieDao.addToMyList(id)
    }

    fun suggestedMovies(id: String): Flow<List<Movie>>{
        return movieDao.suggestedMovies(id)
    }

}
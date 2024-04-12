package br.com.alura.anyflix.data.repository

import br.com.alura.anyflix.data.model.Movie
import br.com.alura.anyflix.data.network.MovieService
import br.com.alura.anyflix.data.network.toMovie
import br.com.alura.anyflix.data.room.database.AnyflixDatabase
import br.com.alura.anyflix.data.room.entities.MovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepository @Inject constructor(database: AnyflixDatabase, private val service: MovieService) {
    val movieDao = database.movieDao()


    //CAMADA ONLINE
    suspend fun getMoviesFromService(): Flow<List<Movie>> = flow{
        val listMovie = mutableListOf<Movie>()
        for (movieResponse in service.getAll()){
            listMovie.add(movieResponse.toMovie())
        }
        emit(listMovie)
    }

    //CAMADA OFFLINE
    suspend fun insertMovie(movie: MovieEntity){
        return movieDao.save(movie)
    }

    fun getAllMovies(): Flow<List<MovieEntity>>{
        return movieDao.findAll()
    }

    fun myMovieList(): Flow<List<MovieEntity>>{
        return movieDao.myList()
    }

    suspend fun removeFromMyList(id: String){
        return movieDao.removeFromMyList(id)
    }

    suspend fun addToMyList(id: String){
        return movieDao.addToMyList(id)
    }

    fun findMovieById(id: String): Flow<MovieEntity>{
        return movieDao.findMovieById(id)
    }

    fun suggestedMovies(id: String): Flow<List<Movie>>{
        return movieDao.suggestedMovies(id)
    }

}
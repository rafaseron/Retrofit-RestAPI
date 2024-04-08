package br.com.alura.anyflix.room.repository

import br.com.alura.anyflix.model.Movie
import br.com.alura.anyflix.room.database.AnyflixDatabase
import br.com.alura.anyflix.room.entities.MovieEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepository @Inject constructor(database: AnyflixDatabase) {
    val movieDao = database.movieDao()

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
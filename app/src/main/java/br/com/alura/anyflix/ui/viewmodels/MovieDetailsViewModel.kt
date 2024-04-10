package br.com.alura.anyflix.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alura.anyflix.room.dao.MovieDao
import br.com.alura.anyflix.room.entities.toMovie
import br.com.alura.anyflix.model.Movie
import br.com.alura.anyflix.navigation.movieIdArgument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject




sealed class MovieDetailsUiState {
    object Loading : MovieDetailsUiState()
    data class Success(val movie: Movie, val suggestedMovies: List<Movie> = emptyList()): MovieDetailsUiState()
}

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle, private val dao: MovieDao): ViewModel() {

    private var currentUiStateJob: Job? = null
    private val _uiState = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadUiState()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadUiState() {
        currentUiStateJob?.cancel()
        currentUiStateJob = viewModelScope.launch {
            dao.findMovieById(
                requireNotNull(
                    savedStateHandle[movieIdArgument]
                )
            ).onStart {
                _uiState.update { MovieDetailsUiState.Loading }
            }.map {
                it.toMovie()
            }.flatMapLatest { movie ->
                dao.suggestedMovies(movie.id)
                    .map { suggestedMovies ->
                        MovieDetailsUiState.Success(
                            movie = movie,
                            suggestedMovies = suggestedMovies,
                        )
                    }
            }.collectLatest { uiState ->
                _uiState.emit(uiState)
            }
        }
    }

    suspend fun addToMyList(movie: Movie) {
        dao.addToMyList(movie.id)
    }

    suspend fun removeFromMyList(movie: Movie) {
        dao.removeFromMyList(movie.id)
    }

    fun loadMovie() {
        loadUiState()
    }

}
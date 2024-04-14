package br.com.alura.anyflix.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alura.anyflix.data.room.dao.MovieDao
import br.com.alura.anyflix.data.room.entities.toMovie
import br.com.alura.anyflix.data.model.Movie
import br.com.alura.anyflix.data.repository.MovieRepository
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
class MovieDetailsViewModel @Inject constructor(private val repository: MovieRepository, private val savedStateHandle: SavedStateHandle): ViewModel() {

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
            repository.findMovieById(
                requireNotNull(
                    savedStateHandle[movieIdArgument]
                )
            ).onStart {
                _uiState.update { MovieDetailsUiState.Loading }
            }.flatMapLatest { movie ->
                repository.suggestedMovies(movie.id)
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
        repository.addToMyList(movie.id)
    }

    suspend fun removeFromMyList(movie: Movie) {
        repository.removeFromMyList(movie.id)
    }

    fun loadMovie() {
        loadUiState()
    }

}
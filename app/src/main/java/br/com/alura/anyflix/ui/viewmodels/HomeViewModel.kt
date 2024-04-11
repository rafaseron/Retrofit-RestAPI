package br.com.alura.anyflix.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alura.anyflix.data.room.dao.MovieDao
import br.com.alura.anyflix.data.model.Movie
import br.com.alura.anyflix.data.network.MovieService
import br.com.alura.anyflix.data.network.toMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class HomeUiState {
    object Loading : HomeUiState()
    object Empty : HomeUiState()
    data class Success(val sections: Map<String, List<Movie>> = emptyMap(), val mainBannerMovie: Movie? = null): HomeUiState()

}
@HiltViewModel
class HomeViewModel @Inject constructor(private val dao: MovieDao, private val service: MovieService): ViewModel() {

    private var currentUiStateJob: Job? = null
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadUiState()
    }

    private fun loadUiState() {
        currentUiStateJob?.cancel()
        currentUiStateJob = viewModelScope.launch {
            //dao.findAll()
            flow {
                val listMovieResponse = service.getAll()
                val listMovies = mutableListOf<Movie>()

                for (movieResponse in listMovieResponse){
                    val latestValue = movieResponse.toMovie()
                    listMovies.add(latestValue)
                }

                emit(listMovies)
                /*
                val listMovies = response.map {
                    movieResponse ->
                    movieResponse.toMovie()
                }
                emit(listMovies)
                */
            }
                .onStart {
                    _uiState.update { HomeUiState.Loading }
                }
                .map { listMovies ->
                    if (listMovies.isEmpty()) {
                        emptyMap()
                    } else {
                        createSections(listMovies)
                    }
                }.collectLatest { sections ->
                    if (sections.isEmpty()) {
                        _uiState.update {
                            HomeUiState.Empty
                        }
                    } else {
                        val movie = sections
                            .entries.random()
                            .value.random()
                        _uiState.update {
                            HomeUiState.Success(
                                sections = sections,
                                mainBannerMovie = movie
                            )
                        }
                    }
                }
        }
    }

    fun loadSections() {
        loadUiState()
    }

    private fun createSections(movies: List<Movie>) = mapOf(
        "Em alta" to movies.shuffled().take(7),
        "Novidades" to movies.shuffled().take(7),
        "Continue assistindo" to movies.shuffled().take(7)
    )

}
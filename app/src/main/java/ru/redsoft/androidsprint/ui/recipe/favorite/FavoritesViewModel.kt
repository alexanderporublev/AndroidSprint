package ru.redsoft.androidsprint.ui.recipe.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.redsoft.androidsprint.data.network.RecipesRepository
import ru.redsoft.androidsprint.model.Recipe
import javax.inject.Inject

data class FavoritesUiState(
    val recipesList: List<Recipe> = emptyList(),
    val hasError: Boolean = false
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData(FavoritesUiState())
    val uiState: LiveData<FavoritesUiState>
        get() = _uiState

    private val mutex = Mutex()

    fun init() {
        viewModelScope.launch {
            val favorites = mutex.withLock {
                recipesRepository.getFavoritesRecipes()
            }

            _uiState.postValue(
                _uiState.value?.copy(
                    recipesList = favorites,
                    hasError = false
                )
            )

        }
    }
}
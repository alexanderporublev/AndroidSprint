package ru.redsoft.androidsprint.ui.recipe.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import ru.redsoft.androidsprint.RecipesPreferences
import ru.redsoft.androidsprint.data.network.RecipesRepository
import ru.redsoft.androidsprint.model.Recipe

data class FavoritesUiState(
    val recipesList: List<Recipe> = emptyList(),
    val hasError: Boolean = false
)

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableLiveData(FavoritesUiState())
    val uiState: LiveData<FavoritesUiState>
        get() = _uiState

    val preferences: RecipesPreferences by lazy { RecipesPreferences(application.applicationContext) }
    private val recipesRepository =
        RecipesRepository.getInstance() ?: throw IllegalStateException("Couldn't create repository")
    private val mutex = Mutex()

    fun init() {
        viewModelScope.launch {
            val ids = preferences.getFavorites().map { it.toInt() }.toSet()
            val favorites = if (ids.isNotEmpty()) {
                recipesRepository.getRecipesByIds(
                    preferences.getFavorites().map { it.toInt() }.toSet(),
                )
            } else {
                emptyList()
            }
            synchronized(mutex) {
                _uiState.postValue(
                    _uiState.value?.copy(
                        recipesList = favorites ?: emptyList(),
                        hasError = favorites == null
                    )
                )
            }
        }
    }
}
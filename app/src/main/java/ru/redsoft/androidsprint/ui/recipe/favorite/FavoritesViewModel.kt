package ru.redsoft.androidsprint.ui.recipe.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.redsoft.androidsprint.RecipesPreferences
import ru.redsoft.androidsprint.data.network.RecipesRepository
import ru.redsoft.androidsprint.model.Recipe
import ru.redsoft.androidsprint.util.ThreadHelper

data class FavoritesUiState(
    val recipesList: List<Recipe> = emptyList(),
    val hasError: Boolean = false
)

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableLiveData(FavoritesUiState())
    val uiState: LiveData<FavoritesUiState>
        get() = synchronized(mutex) {
            return _uiState
        }

    val preferences: RecipesPreferences by lazy { RecipesPreferences(application.applicationContext) }
    private val recipesRepository = RecipesRepository.INSTANCE
    private val mutex = Mutex()

    fun init() {
        ThreadHelper.threadPool.execute {
                val favorites = recipesRepository.getRecipesByIds(
                    preferences.getFavorites().map { it.toInt() }.toSet(),
                )
            synchronized(mutex) {
                _uiState.postValue(
                    _uiState.value?.copy(
                        recipesList = favorites?: emptyList(),
                        hasError = favorites == null
                    )
                )
            }
        }
    }
}
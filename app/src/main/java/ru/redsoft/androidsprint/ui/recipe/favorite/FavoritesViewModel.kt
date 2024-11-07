package ru.redsoft.androidsprint.ui.recipe.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.redsoft.androidsprint.RecipesPreferences
import ru.redsoft.androidsprint.data.stubs.STUB
import ru.redsoft.androidsprint.model.Recipe

data class FavoritesUiState(
    val recipesList: List<Recipe> = emptyList()
)

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableLiveData(FavoritesUiState())
    val uiState: LiveData<FavoritesUiState> = _uiState
    val preferences: RecipesPreferences by lazy { RecipesPreferences(application.applicationContext) }

    fun init() {
        _uiState.value = _uiState.value?.copy(
            recipesList = STUB.getRecipeByIds(
                preferences.getFavorites().map { it.toInt() }.toSet()
            )
        )
    }
}
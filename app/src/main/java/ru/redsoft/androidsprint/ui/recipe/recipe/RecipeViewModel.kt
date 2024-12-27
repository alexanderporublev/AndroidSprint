package ru.redsoft.androidsprint.ui.recipe.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
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

data class RecipeUiState(
    val recipe: Recipe? = null,
    val portionsCount: Int = 1,
    val hasError: Boolean = false,
)

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData(RecipeUiState())
    val uiState: LiveData<RecipeUiState> get() = _uiState

    private val mutex = Mutex()

    fun loadRecipe(recipe: Recipe) {
        _uiState.value =
            _uiState.value?.copy(
                recipe = recipe,
            )
    }

    fun setPortionsCount(count: Int) {
        _uiState.value = _uiState.value?.copy(portionsCount = count)
    }

    fun onFavoritesClicked() = viewModelScope.launch {
        val recipe = _uiState.value?.recipe ?: throw IllegalStateException("No any recipes")
        val newRecipe = recipe.copy(isFavorite = !recipe.isFavorite)
        mutex.withLock { recipesRepository.updateRecipe(newRecipe) }
        _uiState.postValue(
            _uiState.value?.copy(
                recipe = newRecipe,
            )
        )
    }

}
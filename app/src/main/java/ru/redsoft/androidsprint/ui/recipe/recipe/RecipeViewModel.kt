package ru.redsoft.androidsprint.ui.recipe.recipe

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.redsoft.androidsprint.model.Recipe

data class RecipeUiState(
    val recipe: Recipe? = null,
    val portionsCount: Int = 1,
    val isFavorite: Boolean = false,
)

class RecipeViewModel: ViewModel() {
    val _uiState = MutableLiveData(RecipeUiState())
}
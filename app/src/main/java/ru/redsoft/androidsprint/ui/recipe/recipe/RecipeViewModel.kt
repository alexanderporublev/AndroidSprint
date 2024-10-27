package ru.redsoft.androidsprint.ui.recipe.recipe

import android.util.Log
import androidx.lifecycle.LiveData
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

class RecipeViewModel : ViewModel() {
    private val _uiState = MutableLiveData(RecipeUiState())
    val uiState: LiveData<RecipeUiState> get() = _uiState

    init {
        Log.i("!!!", "Start model")
        _uiState.value = RecipeUiState()
        Log.i("!!!", "1")
        _uiState.value = _uiState.value?.copy(isFavorite = true)
    }
}
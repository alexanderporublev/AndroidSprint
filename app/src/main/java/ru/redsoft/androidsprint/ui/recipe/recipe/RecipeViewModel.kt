package ru.redsoft.androidsprint.ui.recipe.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.redsoft.androidsprint.RecipesPreferences
import ru.redsoft.androidsprint.data.stubs.STUB
import ru.redsoft.androidsprint.model.Recipe

data class RecipeUiState(
    val recipe: Recipe? = null,
    val portionsCount: Int = 1,
    val isFavorite: Boolean = false,
    val recipeImage: Drawable? = null,
)

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableLiveData(RecipeUiState())
    val uiState: LiveData<RecipeUiState> get() = _uiState
    val context: Context by lazy { application.applicationContext }

    private val preferences: RecipesPreferences by lazy {
        RecipesPreferences(
            context
        )
    }

    fun loadRecipe(recipeId: Int) {
        //TODO: load from network
        _uiState.value = _uiState.value?.copy(
            recipe = STUB.getRecipeById(recipeId),
            isFavorite = getFavorites().contains(recipeId.toString()),
            recipeImage = imageDrawable(
                STUB.getRecipeById(recipeId)?.imageUrl
                    ?: throw IllegalArgumentException("Not image url provided")
            ),
        )
    }

    fun saveFavorites(ids: Set<String>) {
        preferences.saveFavorites(ids)
        _uiState.value = _uiState.value?.copy(
            isFavorite = ids.contains(_uiState.value?.recipe?.id.toString()),
        )
    }

    fun getFavorites() = preferences.getFavorites()

    fun setPortionsCount(count: Int) {
        _uiState.value = _uiState.value?.copy(portionsCount = count)
    }

    fun onFavoritesClicked() {
        val favorites = getFavorites()
        val currentRecipeId = _uiState.value?.recipe?.id.toString()
        if (currentRecipeId.isNotEmpty() && favorites.contains(currentRecipeId))
            saveFavorites(favorites - currentRecipeId)
        else
            saveFavorites(favorites + currentRecipeId)
    }

    private fun imageDrawable(imageUrl: String): Drawable? =
        Drawable.createFromStream(context.assets?.open(imageUrl), null)
}
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
import kotlinx.coroutines.sync.Mutex
import ru.redsoft.androidsprint.RecipesPreferences
import ru.redsoft.androidsprint.data.network.RecipesRepository
import ru.redsoft.androidsprint.model.Recipe
import ru.redsoft.androidsprint.util.ThreadHelper
import java.io.FileNotFoundException
import java.lang.Error

data class RecipeUiState(
    val recipe: Recipe? = null,
    val portionsCount: Int = 1,
    val isFavorite: Boolean = false,
    val recipeImage: Drawable? = null,
    val hasError: Boolean = false,
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

    private val recipesRepository = RecipesRepository.INSTANCE
    private val mutex = Mutex()

    fun loadRecipe(recipeId: Int) {
        ThreadHelper.threadPool.execute {
            val recipe = recipesRepository.getRecipeById(recipeId)

            synchronized(mutex){
                _uiState.postValue(
                    _uiState.value?.copy(
                        recipe = recipe,
                        isFavorite = getFavorites().contains(recipeId.toString()),
                        recipeImage = try {
                            imageDrawable(
                                recipe?.imageUrl
                                    ?: throw IllegalArgumentException("Not image url provided")
                            )
                        } catch (e: FileNotFoundException) {
                            null
                        },
                    )
                )
            }
        }
    }

    fun saveFavorites(ids: Set<String>) {
        preferences.saveFavorites(ids)
        synchronized(mutex) {
            _uiState.value = _uiState.value?.copy(
                isFavorite = ids.contains(_uiState.value?.recipe?.id.toString()),
            )
        }
    }

    fun getFavorites() = preferences.getFavorites()

    fun setPortionsCount(count: Int) {
        synchronized(mutex) {
            _uiState.value = _uiState.value?.copy(portionsCount = count)
        }
    }

    fun onFavoritesClicked() {
        val favorites = getFavorites()

        val currentRecipeId = synchronized(mutex) { _uiState.value?.recipe?.id.toString() }

        if (currentRecipeId.isNotEmpty() && favorites.contains(currentRecipeId))
            saveFavorites(favorites - currentRecipeId)
        else
            saveFavorites(favorites + currentRecipeId)
    }

    private fun imageDrawable(imageUrl: String): Drawable? =
        Drawable.createFromStream(context.assets?.open(imageUrl), null)
}
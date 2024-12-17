package ru.redsoft.androidsprint.ui.recipieslist

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.redsoft.androidsprint.data.network.RecipesRepository
import ru.redsoft.androidsprint.model.Category
import ru.redsoft.androidsprint.model.Recipe

data class RecipesListUiState(
    val category: Category? = null,
    val recipesList: List<Recipe> = emptyList(),
    val hasError: Boolean = false,
)

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableLiveData(RecipesListUiState())
    private val context: Context by lazy { application.applicationContext }

    val uiState: LiveData<RecipesListUiState>
        get() = _uiState

    private val recipesRepository =
        RecipesRepository.getInstance() ?: throw IllegalStateException("Couldn't create repository")
    private val mutex = Mutex()


    fun init(category: Category) {
        viewModelScope.launch {
            val recipesList = mutex.withLock {
                val recipesList = recipesRepository.getRecipesByCategoryId(category.id)
                recipesList?.forEach {
                    recipesRepository.insertRecipe(it.copy(categoryId = category.id))
                }
                recipesList ?: recipesRepository.getRecipesByCategoryIdFromCache(category.id)
            }

            _uiState.postValue(
                _uiState.value?.copy(
                    category = category,
                    recipesList = recipesList,
                    hasError = recipesList.isEmpty(),
                )
            )
        }
    }
}
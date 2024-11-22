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

    private val recipesRepository = RecipesRepository.INSTANCE
    private val mutex = Mutex()


    fun init(category: Category) {
        viewModelScope.launch{
            val recipesList = recipesRepository.getRecipesByCategoryId(category.id)
            synchronized(mutex) {
                _uiState.postValue(
                    _uiState.value?.copy(
                        category = category,
                        recipesList = recipesList ?: emptyList(),
                        hasError = recipesList == null,
                    )
                )
            }
        }
    }
}
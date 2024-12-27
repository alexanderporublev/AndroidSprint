package ru.redsoft.androidsprint.ui.recipieslist

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
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

class RecipesListViewModel(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData(RecipesListUiState())

    val uiState: LiveData<RecipesListUiState>
        get() = _uiState

    private val mutex = Mutex()


    fun init(category: Category) {
        viewModelScope.launch {
            val recipesList = mutex.withLock {
                val recipesList = recipesRepository.getRecipesByCategoryId(category.id)
                var jobList = emptyList<Job>().toMutableList()
                recipesList?.forEach {
                    jobList += launch {
                        recipesRepository.insertRecipe(it.copy(categoryId = category.id))
                    }
                }
                jobList.forEach { it.join() }
                recipesRepository.getRecipesByCategoryIdFromCache(category.id)
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
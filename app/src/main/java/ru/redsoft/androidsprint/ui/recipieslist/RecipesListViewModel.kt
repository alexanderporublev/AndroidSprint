package ru.redsoft.androidsprint.ui.recipieslist

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.sync.Mutex
import ru.redsoft.androidsprint.data.network.RecipesRepository
import ru.redsoft.androidsprint.model.Category
import ru.redsoft.androidsprint.model.Recipe
import ru.redsoft.androidsprint.util.ThreadProvider

data class RecipesListUiState(
    val category: Category? = null,
    val categoryImage: Drawable? = null,
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
        ThreadProvider.threadPool.execute {
            val recipesList = recipesRepository.getRecipesByCategoryId(category.id)
            synchronized(mutex) {
                _uiState.postValue(
                    _uiState.value?.copy(
                        category = category,
                        categoryImage = Drawable.createFromStream(
                            context.assets?.open(category.imageUrl),
                            null
                        ),
                        recipesList = recipesList ?: emptyList(),
                        hasError = recipesList == null,
                    )
                )
            }
        }
    }
}
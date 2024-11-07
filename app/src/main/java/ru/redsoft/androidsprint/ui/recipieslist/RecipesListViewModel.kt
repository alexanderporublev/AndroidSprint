package ru.redsoft.androidsprint.ui.recipieslist

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.redsoft.androidsprint.data.stubs.STUB
import ru.redsoft.androidsprint.model.Recipe

data class RecipesListUiState(
    val categoryId: Int = -1,
    val categoryName: String = "",
    val categoryImage: Drawable? = null,
    val recipesList: List<Recipe> = emptyList()
)

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableLiveData(RecipesListUiState())
    private val context: Context by lazy { application.applicationContext }

    var uiState: LiveData<RecipesListUiState> = _uiState

    fun init(categoryId: Int, categoryName: String, categoryImageUrl: String) {
        _uiState.value = _uiState.value?.copy(
            categoryId = categoryId,
            categoryName = categoryName,
            categoryImage = Drawable.createFromStream(context.assets?.open(categoryImageUrl), null),
            recipesList = STUB.getRecipesByCategoryId(categoryId)
        )
    }
}
package ru.redsoft.androidsprint.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.sync.Mutex
import ru.redsoft.androidsprint.data.network.RecipesRepository
import ru.redsoft.androidsprint.model.Category
import ru.redsoft.androidsprint.util.ThreadProvider

data class CategoryListUiState(
    val categoryList: List<Category> = emptyList(),
    val hasError: Boolean = false,
)

class CategoryListViewModel : ViewModel() {
    private val _uiState = MutableLiveData(CategoryListUiState())
    val uiState: LiveData<CategoryListUiState>
        get() =  _uiState

    private val recipesRepository = RecipesRepository.INSTANCE
    private val mutex = Mutex()

    fun init() {
        ThreadProvider.threadPool.execute {
                val categories = recipesRepository.getAllCategories()
            synchronized(mutex) {
                    _uiState.postValue(_uiState.value?.copy(
                        categoryList = categories ?: emptyList(),
                        hasError = categories == null
                    ))
            }
        }
    }
}
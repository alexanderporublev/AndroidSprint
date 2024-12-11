package ru.redsoft.androidsprint.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.redsoft.androidsprint.data.network.RecipesRepository
import ru.redsoft.androidsprint.model.Category

data class CategoryListUiState(
    val categoryList: List<Category> = emptyList(),
    val hasError: Boolean = false,
)

class CategoryListViewModel : ViewModel() {
    private val _uiState = MutableLiveData(CategoryListUiState())
    val uiState: LiveData<CategoryListUiState>
        get() =  _uiState

    private val recipesRepository =
        RecipesRepository.getInstance() ?: throw IllegalStateException("Couldn't create repository")
    private val mutex = Mutex()

    fun init() {
        viewModelScope.launch {
            val cache = recipesRepository.getCategoriesFromCache().toMutableList()
            val categories = recipesRepository.getAllCategories()
            categories?.forEach {
                cache += it
                recipesRepository.addCategoryToCache(it)
            }
            mutex.withLock {
                _uiState.postValue(
                    _uiState.value?.copy(
                        categoryList = cache,
                        hasError = categories == null
                    )
                )
            }
        }
    }

}
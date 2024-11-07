package ru.redsoft.androidsprint.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.redsoft.androidsprint.data.stubs.STUB
import ru.redsoft.androidsprint.model.Category

data class CategoryListUiState(
    val categoryList: List<Category> = emptyList(),
)

class CategoryListViewModel: ViewModel(){
    private val _uiState = MutableLiveData(CategoryListUiState(STUB.getCategories()))
    val uiState: LiveData<CategoryListUiState> get() = _uiState

    fun getCategoryById(id: Int) = _uiState.value?.categoryList?.find { it.id == id }
}
package ru.redsoft.androidsprint.di

import ru.redsoft.androidsprint.data.network.RecipesRepository
import ru.redsoft.androidsprint.ui.category.CategoryListViewModel

class CategoryListViewModelFactory(
    private val recipesRepository: RecipesRepository
) : IFactory<CategoryListViewModel> {
    override fun create(): CategoryListViewModel {
        return CategoryListViewModel(recipesRepository)
    }
}
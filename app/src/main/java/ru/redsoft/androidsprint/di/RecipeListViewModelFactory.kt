package ru.redsoft.androidsprint.di

import ru.redsoft.androidsprint.data.network.RecipesRepository
import ru.redsoft.androidsprint.ui.recipieslist.RecipesListViewModel

class RecipeListViewModelFactory(
    private val recipesRepository: RecipesRepository
) : IFactory<RecipesListViewModel> {
    override fun create(): RecipesListViewModel {
        return RecipesListViewModel(recipesRepository)
    }
}
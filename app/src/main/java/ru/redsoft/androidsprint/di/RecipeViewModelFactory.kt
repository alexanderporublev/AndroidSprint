package ru.redsoft.androidsprint.di

import ru.redsoft.androidsprint.data.network.RecipesRepository
import ru.redsoft.androidsprint.ui.recipe.recipe.RecipeViewModel

class RecipeViewModelFactory(
    private val recipesRepository: RecipesRepository
) : IFactory<RecipeViewModel> {
    override fun create(): RecipeViewModel {
        return RecipeViewModel(recipesRepository)
    }
}
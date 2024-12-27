package ru.redsoft.androidsprint.di

import ru.redsoft.androidsprint.data.network.RecipesRepository
import ru.redsoft.androidsprint.ui.recipe.favorite.FavoritesViewModel

class FavoritesViewModelFactory(
    private val recipesRepository: RecipesRepository
) : IFactory<FavoritesViewModel> {
    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(recipesRepository)
    }
}
package ru.redsoft.androidsprint.ui.recipe.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.redsoft.androidsprint.R
import ru.redsoft.androidsprint.ui.recipieslist.RecipesListFragment.Companion.ARG_RECIPE_ID
import ru.redsoft.androidsprint.databinding.FragmentFavoritesBinding
import ru.redsoft.androidsprint.ui.recipe.recipe.RecipeFragment
import ru.redsoft.androidsprint.ui.recipieslist.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private val recipesListAdapter = RecipesListAdapter(emptyList()).also { adapter ->
        adapter.onItemClickCallback = {
            openRecipeByRecipeId(it)
        }
    }
    private val favoritesViewModel: FavoritesViewModel by viewModels()

    private val binding: FragmentFavoritesBinding by lazy {
        FragmentFavoritesBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvRecipes.adapter = recipesListAdapter
        observeData()
        favoritesViewModel.init()
    }

    fun observeData() = favoritesViewModel.uiState.observe(viewLifecycleOwner) { state ->

        if (state.recipesList.isEmpty()) {
            binding.rvRecipes.visibility = View.GONE
            binding.emptyFavoritesPlaceHolder.visibility = View.VISIBLE
        } else {
            binding.rvRecipes.visibility = View.VISIBLE
            binding.emptyFavoritesPlaceHolder.visibility = View.GONE
            recipesListAdapter.recipesList = state.recipesList
        }
    }

    private fun openRecipeByRecipeId(id: Int) {
        val action = FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(id)
        findNavController().navigate(action)
    }
}
package ru.redsoft.androidsprint.ui.recipieslist

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.redsoft.androidsprint.R
import ru.redsoft.androidsprint.databinding.FragmentRecipesListBinding
import ru.redsoft.androidsprint.data.stubs.STUB
import ru.redsoft.androidsprint.ui.category.CategoriesListFragment
import ru.redsoft.androidsprint.ui.recipe.recipe.RecipeFragment

class RecipesListFragment : Fragment() {
    private val recipesListAdapter = RecipesListAdapter(emptyList()).also { adapter ->
        adapter.onItemClickCallback = {
            openRecipeByRecipeId(it)
        }
    }

    private val recipesListViewModel: RecipesListViewModel by viewModels()

    val binding: FragmentRecipesListBinding by lazy {
        FragmentRecipesListBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvRecipes.adapter = recipesListAdapter
        observeState()
        arguments?.let {
            recipesListViewModel.init(
                it.getInt(CategoriesListFragment.ARG_CATEGORY_ID),
                it.getString(CategoriesListFragment.ARG_CATEGORY_NAME) ?: "",
                it.getString(CategoriesListFragment.ARG_CATEGORY_IMAGE_URL) ?: ""
            )
        } ?: throw Exception("No arguments has been provided")
    }

    private fun observeState() = recipesListViewModel.uiState.observe(viewLifecycleOwner) { state ->
        binding.headerImageView.setImageDrawable(state.categoryImage)
        binding.headerImageView.contentDescription =
            getString(R.string.category_recipes_image, state.categoryName)
        binding.categoryNameTextView.text = state.categoryName
        recipesListAdapter.recipesList = state.recipesList
    }

    private fun openRecipeByRecipeId(id: Int) {
        findNavController().navigate(R.id.action_recipesListFragment_to_recipeFragment, bundleOf(ARG_RECIPE_ID to id))
    }

    companion object {
        const val ARG_RECIPE_ID = "arg_recipe_id"
    }
}
package ru.redsoft.androidsprint.ui.recipieslist

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.redsoft.androidsprint.R
import ru.redsoft.androidsprint.databinding.FragmentRecipesListBinding
import ru.redsoft.androidsprint.ui.category.CategoriesListFragment
import ru.redsoft.androidsprint.ui.recipe.recipe.RecipeFragment

class RecipesListFragment : Fragment() {
    private val recipesListAdapter = RecipesListAdapter(emptyList()).also { adapter ->
        adapter.onItemClickCallback = {
            openRecipeByRecipeId(it)
        }
    }

    private val recipesListViewModel: RecipesListViewModel by viewModels()
    private val args: RecipesListFragmentArgs by navArgs()

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
        recipesListViewModel.init(args.category?:throw IllegalArgumentException("No category provided"))
    }

    private fun observeState() = recipesListViewModel.uiState.observe(viewLifecycleOwner) { state ->
        if (state.hasError) {
            Toast.makeText(
                activity?.applicationContext,
                "Ошибка чтения списка рецептов",
                Toast.LENGTH_SHORT
            ).show()
            return@observe
        }
        binding.headerImageView.setImageDrawable(state.categoryImage)
        binding.headerImageView.contentDescription =
            getString(R.string.category_recipes_image, state.category?.title)
        binding.categoryNameTextView.text = state.category?.title
        recipesListAdapter.recipesList = state.recipesList
    }

    private fun openRecipeByRecipeId(id: Int) {
        val action = RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(id)
        findNavController().navigate(action)
    }

}
package ru.redsoft.androidsprint

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.transition.Visibility
import ru.redsoft.androidsprint.RecipesListFragment.Companion.ARG_RECIPE
import ru.redsoft.androidsprint.databinding.FragmentFavoritesBinding
import ru.redsoft.androidsprint.stubs.STUB

class FavoritesFragment : Fragment() {
    private val preferences: RecipesPreferences by lazy {
        RecipesPreferences(
            context ?: throw Exception("Not activity")
        )
    }

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
        initUI()
    }

    fun initUI() {
        val favoriteRecipes =
            STUB.getRecipeByIds(preferences.getFavorites().map { it.toInt() }.toSet())
        if (favoriteRecipes.isEmpty()) {
            binding.rvRecipes.visibility = View.GONE
            binding.emptyFavoritesPlaceHolder.visibility = View.VISIBLE
        } else {
            binding.rvRecipes.visibility = View.VISIBLE
            binding.emptyFavoritesPlaceHolder.visibility = View.GONE
            binding.rvRecipes.adapter =
                RecipesListAdapter(favoriteRecipes).also { adapter ->
                    adapter.onItemClickCallback = { openRecipeByRecipeId(it) }
                }
        }
    }

    private fun openRecipeByRecipeId(id: Int) {
        STUB.getRecipeById(id)?.let {
            parentFragmentManager.commit {
                val bundle = bundleOf(ARG_RECIPE to it)
                setReorderingAllowed(true)
                replace<RecipeFragment>(R.id.fragmentContainerView, args = bundle)
            }
        }
    }
}
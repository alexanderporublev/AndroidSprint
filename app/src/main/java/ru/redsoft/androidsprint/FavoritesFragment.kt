package ru.redsoft.androidsprint

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.redsoft.androidsprint.RecipesListFragment.Companion.ARG_RECIPE
import ru.redsoft.androidsprint.databinding.FragmentFavoritesBinding
import ru.redsoft.androidsprint.stubs.STUB

class FavoritesFragment : Fragment() {
    private val preferences: RecipesPreferences by lazy {
        RecipesPreferences(
            activity ?: throw Exception("Not activity")
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
        initRecycler()
    }

    fun initRecycler() {
        binding.rvRecipes.adapter =
            RecipesListAdapter(STUB.getRecipeByIds(preferences.getFavorites().map { it.toInt() }
                .toSet())).also { adapter ->
                adapter.onItemClickCallback = { openRecipeByRecipeId(it) }
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
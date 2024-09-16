package ru.redsoft.androidsprint

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.redsoft.androidsprint.CategoriesListFragment.Companion.ARG_CATEGORY_ID
import ru.redsoft.androidsprint.databinding.FragmentRecipesListBinding
import ru.redsoft.androidsprint.stubs.STUB

class RecipesListFragment : Fragment() {
    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

    val binding: FragmentRecipesListBinding by lazy { FragmentRecipesListBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            categoryId = it.getInt(CategoriesListFragment.ARG_CATEGORY_ID)
            categoryName = it.getString(CategoriesListFragment.ARG_CATEGORY_NAME)
            categoryImageUrl = it.getString(CategoriesListFragment.ARG_CATEGORY_IMAGE_URL)
        } ?: throw Exception("No arguments has been provided")
        categoryImageUrl?.let { categoryImageUrl ->
            val categoryImageDrawable = Drawable.createFromStream(
                binding.root.context.getAssets().open(categoryImageUrl), null
            )
            binding.headerImageView.setImageDrawable(categoryImageDrawable)
            binding.headerImageView.contentDescription = getString(R.string.category_recipes_image, categoryName)
            }
        binding.categoryNameTextView.text = categoryName?:""
        initRecycler()
    }

    private fun initRecycler() {
        binding.rvRecipes.adapter = RecipesListAdapter(STUB.getRecipesByCategoryId(categoryId?:-1)).also { adapter ->
            adapter.onItemClickCallback = {
                openRecipeByRecipeId(it)
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

    companion object {
        const val ARG_RECIPE = "recipe"
    }
}
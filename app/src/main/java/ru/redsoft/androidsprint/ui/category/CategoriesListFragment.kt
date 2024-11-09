package ru.redsoft.androidsprint.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import ru.redsoft.androidsprint.R
import ru.redsoft.androidsprint.ui.recipieslist.RecipesListFragment
import ru.redsoft.androidsprint.databinding.FragmentCategoriesListBinding
import ru.redsoft.androidsprint.data.stubs.STUB

class CategoriesListFragment : Fragment() {
    val categoryListAdapter = CategoriesListAdapter().also { adapter ->
        adapter.onItemClickCallback = { openRecipesByCategoryId(it.id) }
    }
    private val viewModel: CategoryListViewModel by viewModels()
    private val binding: FragmentCategoriesListBinding by lazy {
        FragmentCategoriesListBinding.inflate(layoutInflater)
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
        onDataChanged()
    }

    private fun onDataChanged() = viewModel.uiState.observe(viewLifecycleOwner) {
        categoryListAdapter.categoriesList =
            viewModel.uiState.value?.categoryList ?: emptyList()
    }

    fun initRecycler() {
        binding.rvCategories.adapter = categoryListAdapter
    }


    private fun openRecipesByCategoryId(id: Int) {
        viewModel.getCategoryById(id)?.let {
            parentFragmentManager.commit {
                val bundle = bundleOf(
                    ARG_CATEGORY_ID to it.id,
                    ARG_CATEGORY_NAME to it.title,
                    ARG_CATEGORY_IMAGE_URL to it.imageUrl
                )
                setReorderingAllowed(true)
                replace<RecipesListFragment>(R.id.fragmentContainerView, args = bundle)
            }
        }
    }

    companion object {
        const val ARG_CATEGORY_ID = "categoryId"
        const val ARG_CATEGORY_NAME = "categoryName"
        const val ARG_CATEGORY_IMAGE_URL = "categoryImageUrl"
    }
}
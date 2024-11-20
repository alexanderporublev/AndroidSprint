package ru.redsoft.androidsprint.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import ru.redsoft.androidsprint.R
import ru.redsoft.androidsprint.ui.recipieslist.RecipesListFragment
import ru.redsoft.androidsprint.databinding.FragmentCategoriesListBinding

class CategoriesListFragment : Fragment() {
    val categoryListAdapter = CategoriesListAdapter()

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

    private fun onDataChanged() = viewModel.uiState.observe(viewLifecycleOwner) { state ->
        if (state.hasError) {
            Toast.makeText(
                activity?.applicationContext,
                "Ошибка чтения категорий",
                Toast.LENGTH_SHORT
            ).show()
            return@observe
        }

        categoryListAdapter.categoriesList =
            viewModel.uiState.value?.categoryList ?: emptyList()

        categoryListAdapter.onItemClickCallback = { openRecipesByCategoryId(it.id) }
    }

    fun initRecycler() {
        binding.rvCategories.adapter = categoryListAdapter
    }


    private fun openRecipesByCategoryId(id: Int) {
        viewModel.getCategoryById(id)?.let {
            val action = CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(it)
            findNavController().navigate(action)
        }?:throw IllegalArgumentException("No category with provided id $id")
    }

}
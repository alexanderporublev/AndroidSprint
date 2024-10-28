package ru.redsoft.androidsprint.ui.recipe.recipe

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.redsoft.androidsprint.R
import ru.redsoft.androidsprint.ui.recipieslist.RecipesListFragment
import ru.redsoft.androidsprint.RecipesPreferences
import ru.redsoft.androidsprint.databinding.FragmentRecipeBinding
import ru.redsoft.androidsprint.model.Recipe

class RecipeFragment : Fragment() {

    private var recipeId: Int? = null
    private val preferences: RecipesPreferences by lazy {
        RecipesPreferences(
            context ?: throw Exception("Not context")
        )
    }
    val binding: FragmentRecipeBinding by lazy { FragmentRecipeBinding.inflate(layoutInflater) }

    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            recipeId = it.getInt(RecipesListFragment.ARG_RECIPE_ID)
        } ?: throw IllegalArgumentException("No arguments has been provided")
        initUI()
        viewModel.loadRecipe(
            recipeId ?: throw IllegalArgumentException("No recipeid has been provided")
        )
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initUI() = viewModel.uiState.observe(viewLifecycleOwner) { state ->
        if (state.recipe == null)
            return@observe
        binding.recipeNameTextView.text = state.recipe.title

        val divider = MaterialDividerItemDecoration(
            binding.rvIngredients.context,
            MaterialDividerItemDecoration.VERTICAL
        ).also { divider ->
            context?.resources?.let { resources ->
                divider.dividerColor = resources.getColor(R.color.gray_divider, context?.theme)
                divider.dividerThickness = 1
                divider.isLastItemDecorated = false
                divider.dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.half_margin)
                divider.dividerInsetStart = resources.getDimensionPixelSize(R.dimen.half_margin)
            }
        }
        binding.headerImageView.setImageDrawable(state.recipeImage)

        val ingredientsAdapter = IngredientsAdapter(state.recipe.ingredients)
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvIngredients.addItemDecoration(divider)

        binding.rvMethod.adapter = MethodAdapter(state.recipe.method)
        binding.rvMethod.addItemDecoration(divider)

        context?.resources?.let { resources ->
            binding.portionsCountTextView.text = resources.getString(
                R.string.portions_count,
                resources.getInteger(R.integer.min_portions_count)
            )
        }

        binding.portionsCountTextView.text =
            resources.getString(R.string.portions_count, state.portionsCount)
        ingredientsAdapter.updateIngredients(state.portionsCount)


        binding.portionsCountSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.setPortionsCount(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) = Unit

            override fun onStopTrackingTouch(seekBar: SeekBar) = Unit
        })

        switchFavoriteIcon(state.isFavorite)
        binding.addToFavoriteButton.setOnClickListener {
            preferences.getFavorites().let { favorites ->
                if (state.isFavorite)
                    viewModel.saveFavorites(favorites - state.recipe.id.toString())
                else
                    viewModel.saveFavorites(favorites + state.recipe.id.toString())
            }
        }

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun switchFavoriteIcon(isFavorite: Boolean) =
        binding.addToFavoriteButton.setImageDrawable(
            context?.resources?.getDrawable(
                if (isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_empty,
                context?.theme
            )
        )


}
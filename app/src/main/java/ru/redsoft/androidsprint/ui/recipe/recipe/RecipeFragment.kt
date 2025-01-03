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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import ru.redsoft.androidsprint.R
import ru.redsoft.androidsprint.RecipesApplication
import ru.redsoft.androidsprint.ui.recipieslist.RecipesListFragment
import ru.redsoft.androidsprint.data.network.ImageDownloadService
import ru.redsoft.androidsprint.databinding.FragmentRecipeBinding
import ru.redsoft.androidsprint.model.Recipe
import java.io.FileNotFoundException
import java.lang.IllegalStateException

@AndroidEntryPoint
class RecipeFragment : Fragment() {
    private val ingredientsAdapter = IngredientsAdapter(emptyList())
    private val methodAdapter = MethodAdapter(emptyList())
    private val args: RecipeFragmentArgs by navArgs()

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

        initUI()
        observeData()
        try {
            viewModel.loadRecipe(
                args.recipe ?: throw IllegalArgumentException("Recipe has not been provided")
            )
        } catch (e: IllegalArgumentException) {
            Toast.makeText(
                activity?.applicationContext,
                e.message,
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun observeData() = viewModel.uiState.observe(viewLifecycleOwner) { state ->
        if (state.hasError) {
            Toast.makeText(
                activity?.applicationContext,
                getString(R.string.error_read_recipe),
                Toast.LENGTH_SHORT
            ).show()
            return@observe
        }
        if (state.recipe == null)
            return@observe
        binding.recipeNameTextView.text = state.recipe.title

        ImageDownloadService.INSTANCE.loadImage(
            state.recipe.imageUrl,
            context ?: throw IllegalStateException("Not any activity"),
            binding.headerImageView
        )

        ingredientsAdapter.ingredientsList = state.recipe.ingredients
        methodAdapter.methodsList = state.recipe.method

        binding.portionsCountTextView.text =
            resources.getString(R.string.portions_count, state.portionsCount)
        ingredientsAdapter.updateIngredients(state.portionsCount)

        switchFavoriteIcon(state.recipe.isFavorite)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initUI() {
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

        binding.rvIngredients.addItemDecoration(divider)
        binding.rvMethod.addItemDecoration(divider)

        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvMethod.adapter = methodAdapter

        context?.resources?.let { resources ->
            binding.portionsCountTextView.text = resources.getString(
                R.string.portions_count,
                resources.getInteger(R.integer.min_portions_count)
            )
        }

        binding.portionsCountSeekBar.setOnSeekBarChangeListener(PortionSeekBarListener { progress: Int ->
            viewModel.setPortionsCount(
                progress
            )
        })

        binding.addToFavoriteButton.setOnClickListener { viewModel.onFavoritesClicked() }
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

class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) : OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        onChangeIngredients(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) = Unit

    override fun onStopTrackingTouch(seekBar: SeekBar) = Unit
}
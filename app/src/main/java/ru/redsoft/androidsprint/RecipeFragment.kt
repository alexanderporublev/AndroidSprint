package ru.redsoft.androidsprint

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.redsoft.androidsprint.databinding.FragmentRecipeBinding
import ru.redsoft.androidsprint.models.Recipe

class RecipeFragment: Fragment() {

    private var recipe: Recipe? = null
    private var isFavorite = false

    val binding: FragmentRecipeBinding by lazy { FragmentRecipeBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(RecipesListFragment.ARG_RECIPE, Recipe::class.java)
            } else {
                it.getParcelable(RecipesListFragment.ARG_RECIPE)
            }
        } ?: throw IllegalArgumentException("No arguments has been provided")
        initUI()
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initUI() = recipe?.also {
        binding.recipeNameTextView.text = it.title
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
        binding.headerImageView.setImageDrawable(
            Drawable.createFromStream(
                context?.assets?.open(it.imageUrl), null
            )
        )
        val ingredientsAdapter = IngredientsAdapter(it.ingredients)
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvIngredients.addItemDecoration(divider)

        binding.rvMethod.adapter = MethodAdapter(it.method)
        binding.rvMethod.addItemDecoration(divider)

        context?.resources?.let { resources ->
            binding.portionsCountTextView.text = resources.getString(
                R.string.portions_count,
                resources.getInteger(R.integer.min_portions_count)
            )
        }

        binding.portionsCountSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.portionsCountTextView.text =
                    resources.getString(R.string.portions_count, progress)
                ingredientsAdapter.updateIngredients(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        switchFavoriteIcon()
        binding.addToFavoriteButton.setOnClickListener {
            isFavorite = !isFavorite
            switchFavoriteIcon()
        }

    }

    private fun switchFavoriteIcon() = binding.addToFavoriteButton.setImageDrawable(context?.resources?.getDrawable(if (isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_empty, context?.theme))

}
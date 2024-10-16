package ru.redsoft.androidsprint

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView.Orientation
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.redsoft.androidsprint.databinding.FragmentRecipeBinding
import ru.redsoft.androidsprint.models.Recipe

class RecipeFragment : Fragment() {

    private var recipe: Recipe? = null
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
                divider.dividerInsetEnd = 0
                divider.dividerInsetStart = 0
            }
        }
        binding.headerImageView.setImageDrawable(
            Drawable.createFromStream(
                context?.getAssets()?.open(it.imageUrl), null
            )
        )

        binding.rvIngredients.adapter = IngredientsAdapter(it.ingredients)
        binding.rvIngredients.addItemDecoration(divider)

        binding.rvMethod.adapter = MethodAdapter(it.method)
        binding.rvMethod.addItemDecoration(divider)
    }
}
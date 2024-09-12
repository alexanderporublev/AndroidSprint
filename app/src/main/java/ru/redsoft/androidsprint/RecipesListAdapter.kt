package ru.redsoft.androidsprint

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.redsoft.androidsprint.databinding.ItemRecipeBinding
import ru.redsoft.androidsprint.models.Recipe

class RecipesListAdapter (private val recipesList: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {
    var onItemClickCallback: ((Int) -> Unit)? = null

    class ViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        val RecipeTitleView = binding.titleView
        val imageView = binding.headerImageView
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecipeBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.RecipeTitleView.text = recipesList[position].title

        val recipeImageDrawable = Drawable.createFromStream(
            viewHolder.binding.root.context.getAssets().open(recipesList[position].imageUrl), null
        )
        viewHolder.imageView.setImageDrawable(recipeImageDrawable)
        viewHolder.binding.root.setOnClickListener{
            onItemClickCallback?.invoke(recipesList[position].id)
        }
    }

    override fun getItemCount() = recipesList.size
}
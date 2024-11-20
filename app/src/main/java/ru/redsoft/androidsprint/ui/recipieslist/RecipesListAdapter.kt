package ru.redsoft.androidsprint.ui.recipieslist

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ru.redsoft.androidsprint.R
import ru.redsoft.androidsprint.databinding.ItemRecipeBinding
import ru.redsoft.androidsprint.model.Recipe
import java.io.FileNotFoundException

class RecipesListAdapter(recipesList: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {
    var onItemClickCallback: ((Int) -> Unit)? = null

    var recipesList: List<Recipe> = recipesList
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyDataSetChanged()
            }
        }

    class ViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        val recipeTitleView = binding.titleView
        val imageView = binding.headerImageView
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecipeBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.recipeTitleView.text = recipesList[position].title
        val context = viewHolder.binding.root.context
        try {
            val recipeImageDrawable = Drawable.createFromStream(
                context.getAssets().open(recipesList[position].imageUrl), null
            )
            viewHolder.imageView.setImageDrawable(recipeImageDrawable)
        } catch (e: FileNotFoundException){
            Toast.makeText(context, "Не удалось загрузить изображение", Toast.LENGTH_SHORT).show()
        }
        viewHolder.imageView.contentDescription =
            context.getString(R.string.recipe_image, recipesList[position].title)
        viewHolder.binding.root.setOnClickListener {
            onItemClickCallback?.invoke(recipesList[position].id)
        }
    }

    override fun getItemCount() = recipesList.size
}
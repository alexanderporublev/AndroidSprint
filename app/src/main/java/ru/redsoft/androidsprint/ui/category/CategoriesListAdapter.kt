package ru.redsoft.androidsprint.ui.category

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.redsoft.androidsprint.databinding.ItemCategoryBinding
import ru.redsoft.androidsprint.model.Category


class CategoriesListAdapter(private val categoriesList: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {
    var onItemClickCallback: ((Category) -> Unit)? = null

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val categoryTitleView = binding.titleView
        val descriptionView = binding.descriptionView
        val imageView = binding.headerImageView

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.categoryTitleView.text = categoriesList[position].title
        viewHolder.descriptionView.text = categoriesList[position].description

        val categoryImageDrawable = Drawable.createFromStream(
            viewHolder.binding.root.context.getAssets().open(categoriesList[position].imageUrl), null
        )
        viewHolder.imageView.setImageDrawable(categoryImageDrawable)
        viewHolder.binding.root.setOnClickListener{
            onItemClickCallback?.invoke(categoriesList[position])
        }
    }

    override fun getItemCount() = categoriesList.size

}
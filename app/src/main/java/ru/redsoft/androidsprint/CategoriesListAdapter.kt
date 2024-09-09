package ru.redsoft.androidsprint

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.redsoft.androidsprint.databinding.FragmentCategoriesListBinding
import ru.redsoft.androidsprint.databinding.ItemCategoryBinding
import ru.redsoft.androidsprint.models.Category


class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

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
        viewHolder.categoryTitleView.text = dataSet[position].title
        viewHolder.descriptionView.text = dataSet[position].description

        val categoryImageDrawable = Drawable.createFromStream(
            viewHolder.binding.root.context.getAssets().open(dataSet[position].imageUrl), null
        )
        viewHolder.imageView.setImageDrawable(categoryImageDrawable)
    }

    override fun getItemCount() = dataSet.size

}
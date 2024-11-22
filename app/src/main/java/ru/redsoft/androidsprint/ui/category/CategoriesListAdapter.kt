package ru.redsoft.androidsprint.ui.category

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.redsoft.androidsprint.data.network.ImageDownloadService
import ru.redsoft.androidsprint.databinding.ItemCategoryBinding
import ru.redsoft.androidsprint.model.Category


class CategoriesListAdapter() :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {
    var onItemClickCallback: ((Category) -> Unit)? = null
    var categoriesList: List<Category> = emptyList()
        get() = field
        set(value) {
            if (field != value) {
                field = value
                notifyDataSetChanged()
            }
        }

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


        ImageDownloadService.INSTANCE.loadImage(
            categoriesList[position].imageUrl,
            viewHolder.binding.root.context,
            viewHolder.imageView
        )
        viewHolder.binding.root.setOnClickListener {
            onItemClickCallback?.invoke(categoriesList[position])
        }
    }

    override fun getItemCount() = categoriesList.size
}
package ru.redsoft.androidsprint

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.redsoft.androidsprint.models.Category


class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>()  {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val categoryTitleView: TextView
        val descriptionView: TextView
        val imageView: ImageView

        init {
            categoryTitleView = view.findViewById(R.id.titleView)
            descriptionView = view.findViewById(R.id.descriptionView)
            imageView = view.findViewById(R.id.headerImageView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_category, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.categoryTitleView.text = dataSet[position].title
        viewHolder.descriptionView.text = dataSet[position].description

        val d = Drawable.createFromStream(viewHolder.view.context.getAssets().open(dataSet[position].imageUrl), null)
        viewHolder.imageView.setImageDrawable(d)
    }

    override fun getItemCount() = dataSet.size

}
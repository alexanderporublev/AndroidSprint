package ru.redsoft.androidsprint

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.redsoft.androidsprint.databinding.ItemIngredientBinding
import ru.redsoft.androidsprint.models.Ingredient

class IngredientsAdapter(private val ingredientsList: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemIngredientBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = ingredientsList.size


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.ingredientNameTextView.text = ingredientsList[position].description
        viewHolder.binding.ingredientCountTextView.text =
            "${ingredientsList[position].quantity} ${ingredientsList[position].unitOfMeasure}"
    }
}
package ru.redsoft.androidsprint.ui.recipe.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.redsoft.androidsprint.databinding.ItemIngredientBinding
import ru.redsoft.androidsprint.model.Ingredient

class IngredientsAdapter(
    ingredientsList: List<Ingredient>,
) : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var portionsCount: Int = 1

    var ingredientsList = ingredientsList
        get() = field
        set(data: List<Ingredient>) {
            if (data == field)
                return
            field = data
            notifyDataSetChanged()
        }

    class ViewHolder(val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemIngredientBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = ingredientsList.size


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.ingredientNameTextView.text = ingredientsList[position].description

        val quantity = try {
            "${ingredientsList[position].quantity.toInt() * portionsCount}"
        } catch (e: NumberFormatException) {
            try {
                "%.1f".format(ingredientsList[position].quantity.toFloat() * portionsCount)
            } catch (e: NumberFormatException) {
                ingredientsList[position].quantity
            }
        }

        viewHolder.binding.ingredientCountTextView.text =
            "$quantity ${ingredientsList[position].unitOfMeasure}"
    }

    fun updateIngredients(progress: Int) {
        portionsCount = progress
        notifyDataSetChanged()
    }
}
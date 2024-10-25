package ru.redsoft.androidsprint.ui.recipe.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.redsoft.androidsprint.databinding.ItemMethodBinding

class MethodAdapter(val methodsList: List<String>): RecyclerView.Adapter<MethodAdapter.ViewHolder>(){

    class ViewHolder(val binding: ItemMethodBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMethodBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int =  methodsList.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.methodTextView.text = "${position+1}. ${methodsList[position]}"
    }
}
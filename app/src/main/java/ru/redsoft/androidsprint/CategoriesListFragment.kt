package ru.redsoft.androidsprint

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.redsoft.androidsprint.databinding.FragmentCategoriesListBinding


class CategoriesListFragment : Fragment() {

    private val binding: FragmentCategoriesListBinding by lazy {
        FragmentCategoriesListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categories_list, container, false)
    }
}
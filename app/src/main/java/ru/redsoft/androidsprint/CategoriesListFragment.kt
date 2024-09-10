package ru.redsoft.androidsprint

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.redsoft.androidsprint.databinding.FragmentCategoriesListBinding
import ru.redsoft.androidsprint.stubs.STUB


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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    fun initRecycler() {
        binding.rvCategories.adapter = CategoriesListAdapter(STUB.getCategories())
    }
}
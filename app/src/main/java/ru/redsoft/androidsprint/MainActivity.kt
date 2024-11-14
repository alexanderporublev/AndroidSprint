package ru.redsoft.androidsprint

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.NavController
import androidx.navigation.findNavController
import ru.redsoft.androidsprint.databinding.ActivityMainBinding
import ru.redsoft.androidsprint.ui.category.CategoriesListFragment
import ru.redsoft.androidsprint.ui.recipe.favorite.FavoritesFragment

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.categoryButton.setOnClickListener {
            findNavController(R.id.fragmentContainerView).navigate(R.id.categoriesListFragment)
        }

        binding.favoriteButton.setOnClickListener {
            findNavController(R.id.fragmentContainerView).navigate(R.id.favoritesFragment)
        }
    }
}
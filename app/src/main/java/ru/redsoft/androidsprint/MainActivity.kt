package ru.redsoft.androidsprint

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import ru.redsoft.androidsprint.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val navOptions = navOptions {launchSingleTop = true}

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
            findNavController(R.id.fragmentContainerView).navigate(
                R.id.categoriesListFragment,
                null,
                navOptions
            )
        }

        binding.favoriteButton.setOnClickListener {
            findNavController(R.id.fragmentContainerView).navigate(
                R.id.favoritesFragment,
                null,
                navOptions
            )
        }
    }
}
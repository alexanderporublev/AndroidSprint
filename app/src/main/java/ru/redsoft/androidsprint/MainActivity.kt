package ru.redsoft.androidsprint

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import kotlinx.serialization.json.Json
import ru.redsoft.androidsprint.databinding.ActivityMainBinding
import ru.redsoft.androidsprint.model.Category
import ru.redsoft.androidsprint.model.Recipe
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val navOptions = navOptions {
        launchSingleTop = true
        anim{
            enter = androidx.navigation.ui.R.anim.nav_default_enter_anim
            exit = androidx.navigation.ui.R.anim.nav_default_exit_anim
            popEnter = androidx.navigation.ui.R.anim.nav_default_pop_enter_anim
            popExit = androidx.navigation.ui.R.anim.nav_default_pop_exit_anim
        }
    }

    private val threadPool = Executors.newFixedThreadPool(10)

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

        Log.i(TAG, "OnCreate выполняется на потоке ${Thread.currentThread().name}")

        threadPool.execute {
            Log.i(TAG, "Выполняю запрос на потоке ${Thread.currentThread().name}")
            val urlCategories = URL("https://recipes.androidsprint.ru/api/category")
            val connectionCategories = urlCategories.openConnection() as HttpURLConnection
            connectionCategories.connect()
            if (connectionCategories.responseCode == 200) {
                val responseString = connectionCategories.inputStream.bufferedReader().readText()
                val categories = Json.decodeFromString<List<Category>> (responseString)
                Log.i(TAG, "Получено ${categories.size} категорий")
                categories.forEach { category ->
                    threadPool.execute {
                        val urlRecipes = URL("https://recipes.androidsprint.ru/api/category/${category.id}/recipes")
                        val connectionRecipes = urlRecipes.openConnection() as HttpURLConnection
                        connectionRecipes.connect()
                        if (connectionRecipes.responseCode == 200) {
                            val responseString = connectionRecipes.inputStream.bufferedReader().readText()
                            val recipes = Json.decodeFromString<List<Recipe>> (responseString)
                            Log.i(TAG, """
                                Получен список рецептов категории ${category.title}:
                                ${recipes.joinToString(", "){it.title}}
                            """.trimIndent())
                        }
                    }
                }
            }
        }
    }

    companion object {
        val TAG = "MainActivity"
    }
}
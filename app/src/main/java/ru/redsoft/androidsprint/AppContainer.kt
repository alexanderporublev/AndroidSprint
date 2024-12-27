package ru.redsoft.androidsprint

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.redsoft.androidsprint.data.local.AppDatabase
import ru.redsoft.androidsprint.data.network.RecipeApiService
import ru.redsoft.androidsprint.data.network.RecipesRepository
import ru.redsoft.androidsprint.data.network.RecipesRepository.Companion.CONTENT_TYPE
import ru.redsoft.androidsprint.data.network.RecipesRepository.Companion.RECIPE_API_BASE_URL
import ru.redsoft.androidsprint.di.CategoryListViewModelFactory
import ru.redsoft.androidsprint.di.FavoritesViewModelFactory
import ru.redsoft.androidsprint.di.RecipeListViewModelFactory
import ru.redsoft.androidsprint.di.RecipeViewModelFactory

class AppContainer(context: Context) {
    private val db: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "recipe_db"
    ).build()

    private val ioDispatcher = Dispatchers.IO
    private val categoriesDao = db.categoriesDao()
    private val recipesDao = db.recipesDao()

    private val logging = HttpLoggingInterceptor()
        .apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(RECIPE_API_BASE_URL)
            .addConverterFactory(Json.asConverterFactory(CONTENT_TYPE.toMediaType()))
            .client(client)
            .build()

    private val service = retrofit.create(RecipeApiService::class.java)

    val recipesRepository = RecipesRepository(
        service = service,
        dispatcherIO = ioDispatcher,
        categoriesDao = categoriesDao,
        recipesDao = recipesDao,
    )

    val categoriesListViewModelFactory = CategoryListViewModelFactory(recipesRepository)
    val recipesListViewModelFactory = RecipeListViewModelFactory(recipesRepository)
    val recipeViewModelFactory = RecipeViewModelFactory(recipesRepository)
    val favoritesViewModelFactory = FavoritesViewModelFactory(recipesRepository)
}
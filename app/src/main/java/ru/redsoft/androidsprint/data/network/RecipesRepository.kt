package ru.redsoft.androidsprint.data.network

import android.content.Context
import android.util.Log
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import ru.redsoft.androidsprint.IODispatcher
import ru.redsoft.androidsprint.data.local.AppDatabase
import ru.redsoft.androidsprint.data.local.CategoriesDao
import ru.redsoft.androidsprint.data.local.RecipesDao
import ru.redsoft.androidsprint.model.Category
import ru.redsoft.androidsprint.model.Recipe
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.IllegalStateException

class RecipesRepository @Inject constructor(
    private val service: RecipeApiService,
    private val categoriesDao: CategoriesDao,
    private val recipesDao: RecipesDao,
    @IODispatcher private val dispatcherIO: CoroutineDispatcher,
) {
    suspend fun getRecipeById(id: Int): Recipe? = withContext(dispatcherIO) {
        try {
            val response = service.getRecipeById(id).execute()
            response.body()
        } catch (e: Exception) {
            Log.e(TAG, connectionErrorMessage)
            null
        }
    }


    suspend fun getRecipesByIds(ids: Set<Int>): List<Recipe>? = withContext(dispatcherIO) {
        val query = ids.joinToString(",")
        try {
            val response = service.getRecipesByIdList(query).execute()
            response.body()
        } catch (e: Exception) {
            Log.e(TAG, connectionErrorMessage)
            null
        }
    }


    suspend fun getCategoryById(id: Int): Category? = withContext(dispatcherIO) {
        try {
            val response = service.getCategoryById(id).execute()
            response.body()
        } catch (e: Exception) {
            Log.e(TAG, connectionErrorMessage)
            null
        }
    }

    suspend fun getRecipesByCategoryId(id: Int): List<Recipe>? = withContext(dispatcherIO) {
        try {
            val response = service.getRecipesByCategoryId(id).execute()
            response.body()
        } catch (e: Exception) {
            Log.e(TAG, connectionErrorMessage)
            null
        }
    }

    suspend fun getAllCategories(): List<Category>? = withContext(dispatcherIO) {
        try {
            val response = service.getAllCategories().execute()
            response.body()
        } catch (e: Exception) {
            Log.e(TAG, "print" + e.stackTraceToString())
            null
        }
    }

    suspend fun getCategoriesFromCache(): List<Category> = withContext(dispatcherIO) {
        categoriesDao.getAllCategories()
    }

    suspend fun addCategoryToCache(category: Category) = withContext(dispatcherIO) {
        categoriesDao.insertCategory(category)

    }

    suspend fun getRecipeByIdFromCache(id: Int): Recipe? = withContext(dispatcherIO) {
        recipesDao.getRecipeById(id)
    }

    suspend fun getRecipesByCategoryIdFromCache(categoryId: Int): List<Recipe> =
        withContext(dispatcherIO) {
            recipesDao.getRecipesByCategoryId(categoryId)
        }

    suspend fun getFavoritesRecipes(): List<Recipe> = withContext(dispatcherIO) {
        recipesDao.getFavoriteRecipes()
    }

    suspend fun insertRecipe(recipe: Recipe) = withContext(dispatcherIO) {
        recipesDao.insertRecipe(recipe)
    }

    suspend fun updateRecipe(recipe: Recipe) = withContext(dispatcherIO) {
        recipesDao.updateRecipe(recipe)
    }

    companion object {
        val TAG = "RecipesRepository"
        val connectionErrorMessage = "Не удалось подключиться к серверу"
        val RECIPE_API_BASE_URL = "https://recipes.androidsprint.ru/api/"
        val CONTENT_TYPE = "application/json"
    }
}
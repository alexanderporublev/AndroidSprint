package ru.redsoft.androidsprint.data.network

import android.content.Context
import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.redsoft.androidsprint.R
import ru.redsoft.androidsprint.model.Category
import ru.redsoft.androidsprint.model.Recipe

class RecipesRepository private constructor() {
    private val retrofit =
        Retrofit.Builder()
            .baseUrl(RECIPE_API_BASE_URL)
            .addConverterFactory(Json.asConverterFactory(CONTENT_TYPE.toMediaType()))
            .build()

    private val service = retrofit.create(RecipeApiService::class.java)

    fun getRecipeById(id: Int): Recipe? {
        try {
            val response = service.getRecipeById(id).execute()
            return response.body()
        } catch (e: Exception) {
            Log.e(TAG, connectionErrorMessage)
            return null
        }
    }

    fun getRecipesByIds(ids: Set<Int>): List<Recipe>? {
        val query = ids.joinToString(",")
        try {
            val response = service.getRecipesByIdList(query).execute()
            return response.body()
        } catch (e: Exception) {
            Log.e(TAG, connectionErrorMessage)
            return null
        }
    }

    fun getCategoryById(id: Int): Category? {
        try {
            val response = service.getCategoryById(id).execute()
            return response.body()
        } catch (e: Exception) {
            Log.e(TAG, connectionErrorMessage)
            return null
        }
    }

    fun getRecipesByCategoryId(id: Int): List<Recipe>? {
        try {
        val response = service.getRecipesByCategoryId(id).execute()
        return response.body()
        } catch (e: Exception) {
            Log.e(TAG, connectionErrorMessage)
            return null
        }
    }

    fun getAllCategories(): List<Category>? {
        try {
            val response = service.getAllCategories().execute()
            return response.body()
        } catch (e: Exception) {
            Log.e(TAG, connectionErrorMessage)
            return null
        }
    }


    companion object {
        val INSTANCE: RecipesRepository by lazy { RecipesRepository() }
        val TAG = "RecipesRepository"
        val connectionErrorMessage = "Не удалось подключиться к серверу"
        val RECIPE_API_BASE_URL = "https://recipes.androidsprint.ru/api/"
        val CONTENT_TYPE = "application/json"
    }
}
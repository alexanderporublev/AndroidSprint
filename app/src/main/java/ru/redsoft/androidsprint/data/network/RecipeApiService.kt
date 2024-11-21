package ru.redsoft.androidsprint.data.network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.redsoft.androidsprint.model.Category
import ru.redsoft.androidsprint.model.Recipe

interface RecipeApiService {

    @GET("recipe/{id}")
    fun getRecipeById(@Path("id") id: Int): Call<Recipe>

    @GET("recipes")
    fun getRecipesByIdList(@Query(value="ids", encoded=true) ids: String): Call<List<Recipe>>

    @GET("category/{id}")
    fun getCategoryById(@Path("id") id: Int): Call<Category>

    @GET("category/{id}/recipes")
    fun getRecipesByCategoryId(@Path("id") id: Int): Call<List<Recipe>>

    @GET("category")
    fun getAllCategories(): Call<List<Category>>
}
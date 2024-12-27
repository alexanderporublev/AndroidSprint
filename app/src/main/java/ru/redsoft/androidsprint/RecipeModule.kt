package ru.redsoft.androidsprint

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.redsoft.androidsprint.data.local.AppDatabase
import ru.redsoft.androidsprint.data.local.RecipesDao
import ru.redsoft.androidsprint.data.network.RecipeApiService
import ru.redsoft.androidsprint.data.network.RecipesRepository
import ru.redsoft.androidsprint.data.network.RecipesRepository.Companion.CONTENT_TYPE
import ru.redsoft.androidsprint.data.network.RecipesRepository.Companion.RECIPE_API_BASE_URL

@Module
@InstallIn(SingletonComponent::class)
class RecipeModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java, "recipe_db"
        ).build()

    @Provides
    fun provideCategoryDao(appDatabase: AppDatabase) = appDatabase.categoriesDao()

    @Provides
    fun provideRecipesDao(appDatabase: AppDatabase) = appDatabase.recipesDao()
    private val ioDispatcher = Dispatchers.IO

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
            .apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient) = Retrofit.Builder()
        .baseUrl(RECIPE_API_BASE_URL)
        .addConverterFactory(Json.asConverterFactory(CONTENT_TYPE.toMediaType()))
        .client(client)
        .build()

    @Provides
    fun provideService(retrofit: Retrofit) = retrofit.create(RecipeApiService::class.java)
}
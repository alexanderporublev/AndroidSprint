package ru.redsoft.androidsprint.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.redsoft.androidsprint.model.Category
import ru.redsoft.androidsprint.model.Recipe

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipe WHERE id=:id")
    fun getRecipeById(id: Int): Recipe?

    @Query("SELECT * FROM recipe WHERE category_id=:categoryId")
    fun getRecipesByCategoryId(categoryId: Int): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: Recipe)
}
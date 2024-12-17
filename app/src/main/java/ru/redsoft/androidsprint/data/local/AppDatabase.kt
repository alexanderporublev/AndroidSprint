package ru.redsoft.androidsprint.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.redsoft.androidsprint.model.Category
import ru.redsoft.androidsprint.model.Ingredient
import ru.redsoft.androidsprint.model.IngredientConverter
import ru.redsoft.androidsprint.model.MethodConverter
import ru.redsoft.androidsprint.model.Recipe

@Database(
    entities = [Category::class, Recipe::class],
    version = 2,
    autoMigrations = [
        AutoMigration (
            from = 1,
            to = 2
       )
    ],
    exportSchema = true,
)
@TypeConverters(IngredientConverter::class, MethodConverter::class)
abstract class AppDatabase  : RoomDatabase(){
    abstract fun categoriesDao(): CategoriesDao
    abstract fun recipesDao(): RecipesDao
}
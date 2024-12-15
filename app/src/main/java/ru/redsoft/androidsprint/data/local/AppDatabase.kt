package ru.redsoft.androidsprint.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.redsoft.androidsprint.model.Category

@Database(entities = [Category::class], version = 1)
abstract class AppDatabase  : RoomDatabase(){
    abstract fun categoriesDao(): CategoriesDao
}
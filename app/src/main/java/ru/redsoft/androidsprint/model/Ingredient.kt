package ru.redsoft.androidsprint.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Parcelize
@Serializable
data class Ingredient(
    val quantity: String,
    val unitOfMeasure: String,
    val description: String,
) : Parcelable

class IngredientConverter{
    @TypeConverter
    fun asString(ingredients: List<Ingredient>): String = Json.encodeToString(ingredients)

    @TypeConverter
    fun fromString(str: String): List<Ingredient> = Json.decodeFromString(str)
}

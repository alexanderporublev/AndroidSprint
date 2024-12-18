package ru.redsoft.androidsprint.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Ingredient(
    val quantity: String,
    val unitOfMeasure: String,
    val description: String,
) : Parcelable

class IngredientConverter{
    @TypeConverter
    fun asString(ingredients: List<Ingredient>): String =
        ingredients.joinToString(";") { "${it.quantity}:${it.unitOfMeasure}:${it.description}" }

    @TypeConverter
    fun fromString(str: String): List<Ingredient> = str.split(";").map {
        val (quantity, unitOfMeasure, description) = it.split(":")
        Ingredient(quantity, unitOfMeasure, description)
    }
}

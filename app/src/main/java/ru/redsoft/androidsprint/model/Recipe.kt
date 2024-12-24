package ru.redsoft.androidsprint.model
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Parcelize
@Serializable
@Entity(
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("category_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Recipe(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "ingredients")
    @TypeConverters(IngredientConverter::class)
    val ingredients: List<Ingredient>,
    @TypeConverters(MethodConverter::class)
    @ColumnInfo(name = "method") val method: List<String>,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @Transient @ColumnInfo(name = "category_id", index = true) val categoryId: Int = -1,
) : Parcelable


class MethodConverter{
    @TypeConverter
    fun asString(ingredients: List<String>): String =
        ingredients.joinToString(";")

    @TypeConverter
    fun fromString(str: String): List<String> = str.split(";")
}

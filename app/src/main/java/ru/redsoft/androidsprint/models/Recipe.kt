package ru.redsoft.androidsprint.models

data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: List<Ingredient>,
)

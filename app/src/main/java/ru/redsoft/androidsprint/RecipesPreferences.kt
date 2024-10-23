package ru.redsoft.androidsprint

import android.app.Activity
import android.content.Context

class RecipesPreferences(val activity: Activity) {
    private val sharedPrefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveFavorites(ids: Set<String>) {
        val editor = sharedPrefs?.edit()
        editor?.putStringSet(PREFS_FAVORITE_IDS, ids)
        editor?.apply()
    }

    fun getFavorites() =
        sharedPrefs.getStringSet(PREFS_FAVORITE_IDS, emptySet())?.let { HashSet<String>(it) }
            ?: HashSet()

    companion object {
        const val PREFS_NAME = "recipes_preferences"
        const val PREFS_FAVORITE_IDS = "favorite_ids"
    }
}
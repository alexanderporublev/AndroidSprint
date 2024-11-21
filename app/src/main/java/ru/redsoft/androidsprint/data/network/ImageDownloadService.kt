package ru.redsoft.androidsprint.data.network

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide

class ImageDownloadService private constructor() {
    fun loadImage(url: String, context: Context, view: ImageView) {
        Glide.with(context).load(BASE_URL + url)
            .placeholder(Drawable.createFromStream(context.assets?.open(PLACEHOLDER_URL), null))
            .error(Drawable.createFromStream(context.assets?.open(ERROR_URL), null))
            .into(view);
    }

    companion object {
        val INSTANCE: ImageDownloadService by lazy { ImageDownloadService() }
        val BASE_URL = "${RecipesRepository.RECIPE_API_BASE_URL}/images/"
        val PLACEHOLDER_URL = "img_placeholder.png"
        val ERROR_URL = "img_error.png"
    }
}
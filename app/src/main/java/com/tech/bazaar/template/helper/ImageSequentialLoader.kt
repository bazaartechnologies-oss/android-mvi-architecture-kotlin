package com.tech.bazaar.template.helper

import android.util.Log
import android.widget.ImageView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.facebook.shimmer.ShimmerFrameLayout
import com.tech.bazaar.template.extention.hide
import com.tech.bazaar.template.extention.show
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.LinkedList
import java.util.Queue
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageSequentialLoader @Inject constructor() {

    var items: Queue<ImageMetaData> = LinkedList()
        private set

    fun add(imageData: ImageMetaData) {
        items.add(imageData)
    }

    fun load() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                while (items.size > 0) {
                    val imageData = items.poll()
                    loadAnImage(imageData!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun loadAnImage(imageData: ImageMetaData) {
        val job = withContext(Dispatchers.IO) {
            imageData.imageView.load(imageData.url) {
                transformations(RoundedCornersTransformation(10f))
            }
        }

        Log.d("ImageSequentialLoader", "Loading.. ${imageData.name}")
        job.await()
        imageData.imageView.show()
        imageData.shimmerView.hide()
        imageData.shimmerView.stopShimmer()
    }

}

data class ImageMetaData(
    val imageView: ImageView,
    val shimmerView: ShimmerFrameLayout,
    val url: String,
    val name: String
)
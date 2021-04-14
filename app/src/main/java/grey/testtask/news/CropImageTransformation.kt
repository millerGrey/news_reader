package grey.testtask.news

import android.graphics.Bitmap
import com.squareup.picasso.Transformation


open class CropImageTransformation(
    private val imageViewWidth: Int,
    private val imageViewHeight: Int
) : Transformation {
    override fun transform(source: Bitmap): Bitmap {
        var result: Bitmap = source

        if (source.height < imageViewHeight || source.width < imageViewWidth) {
            val scaleFactorH = imageViewHeight.toFloat() / source.height.toFloat()
            val scaleFactorW = imageViewWidth.toFloat() / source.width.toFloat()
            val scaleFactor = Math.max(scaleFactorH, scaleFactorW)
            val newW = (source.width * scaleFactor).toInt()
            val newH = (source.height * scaleFactor).toInt()
            result = Bitmap.createScaledBitmap(source, newW, newH, true)
        }

        val x = (result.width - imageViewWidth) / 2
        val y = (result.height - imageViewHeight) / 2
        try {
            result = Bitmap.createBitmap(result, x, y, imageViewWidth, imageViewHeight)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (result != source) {
            source.recycle()
        }
        return result
    }

    override fun key(): String {
        return "cropImage()"
    }
}
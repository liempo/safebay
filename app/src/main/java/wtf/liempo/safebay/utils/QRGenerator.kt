package wtf.liempo.safebay.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.content.ContextCompat
import com.github.sumimakito.awesomeqr.AwesomeQrRenderer
import com.github.sumimakito.awesomeqr.option.RenderOption
import com.github.sumimakito.awesomeqr.option.color.Color
import com.github.sumimakito.awesomeqr.option.logo.Logo
import timber.log.Timber
import wtf.liempo.safebay.R
import java.lang.Exception

object QRGenerator {

    fun generate(context: Context, value: String): Bitmap? {
        val color = Color().apply {
            light = ContextCompat.getColor(
                context, R.color.primary_light)
            dark = ContextCompat.getColor(
                context, R.color.primary_dark)
            background = ContextCompat.getColor(
                context, R.color.secondary)
        }

        val logo = Logo().apply {
            bitmap = BitmapFactory.decodeResource(
                context.resources,
                R.mipmap.ic_launcher)
            borderRadius = 12
            borderWidth = 12
        }

        val options = RenderOption().apply {
            content = value
            size = 768
            borderWidth = 32
            roundedPatterns = true
            this.color = color
            this.logo = logo
        }

        return try {
            AwesomeQrRenderer.render(options).bitmap
        } catch (e: Exception) {
            Timber.e(e, "Something went wrong while rendering QR.")
            return null
        }
    }

}

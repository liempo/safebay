package wtf.liempo.safebay.utils

import android.graphics.Bitmap
import com.github.sumimakito.awesomeqr.AwesomeQrRenderer
import com.github.sumimakito.awesomeqr.option.RenderOption
import timber.log.Timber

object QRGenerator {

    fun generate(value: String): Bitmap? {

        val options = RenderOption().apply {
            content = value
            size = 768
            borderWidth = 32
            roundedPatterns = true
        }

        return try {
            AwesomeQrRenderer.render(options).bitmap
        } catch (e: Exception) {
            Timber.e(e, "Something went wrong while rendering QR.")
            return null
        }
    }

}

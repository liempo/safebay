package wtf.liempo.safebay.utils

import android.graphics.Bitmap
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import timber.log.Timber

object QRGenerator {

    fun generate(value: String): Bitmap? {

        val encoder = QRGEncoder(
            value,
            QRGContents.Type.TEXT,
            512)

        return try {
            encoder.bitmap
        } catch (e: Exception) {
            Timber.e(e, "Something went wrong while rendering QR.")
            return null
        }
    }

}

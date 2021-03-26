package wtf.liempo.safebay.home.ui

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer: ImageAnalysis.Analyzer {

    private val scanner: BarcodeScanner by lazy {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
        BarcodeScanning.getClient(options)
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(proxy: ImageProxy) {
        // Get image from proxy and return if null
        val mediaImage = proxy.image ?: return

        // Create a MLKit image from mediaImage
        val inputImage = InputImage.fromMediaImage(
            mediaImage, proxy.imageInfo.rotationDegrees)

        // Pass inputImage to scanner and listen for success
        scanner.process(inputImage)
            .addOnSuccessListener {
                proxy.close()

            }.addOnFailureListener {
                proxy.close()
            }
    }

}

package wtf.liempo.safebay.ui.home

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.*
import timber.log.Timber

class BarcodeAnalyzer: ImageAnalysis.Analyzer {

    private val scope = CoroutineScope(Dispatchers.Default)
    private var listener: suspend ((String) -> Unit) = {}

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
                // Return if multiple barcode
                if (it.isEmpty()) {
                    proxy.close()
                    return@addOnSuccessListener
                }

                // App doesn't feature multiple barcode processing
                val barcode = it[0].rawValue!!

                // Launch listener suspendably (lol)
                scope.launch {
                    listener.invoke(barcode)
                    proxy.close()
                }

            }.addOnFailureListener {
                Timber.e(it)
                proxy.close()
            }
    }

    fun setResultListener(
        listener: suspend ((String) -> Unit)
    ) {
        this.listener = listener
    }
}

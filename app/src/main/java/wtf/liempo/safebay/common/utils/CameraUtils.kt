package wtf.liempo.safebay.common.utils

import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import java.util.concurrent.Executor
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume

object CameraUtils {

    suspend fun getCameraProvider(
        context: Context,
        executor: Executor
    ): ProcessCameraProvider =
        suspendCoroutine { cont ->
            with (ProcessCameraProvider.getInstance(context)) {
                addListener({
                    cont.resume(get())
                }, executor)
            }
        }
}

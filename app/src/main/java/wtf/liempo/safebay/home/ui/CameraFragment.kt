package wtf.liempo.safebay.home.ui

import android.Manifest.permission.CAMERA
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import wtf.liempo.safebay.R
import wtf.liempo.safebay.databinding.CameraFragmentBinding

class CameraFragment : Fragment() {

    private val vm: HomeViewModel by activityViewModels()
    private var _binding: CameraFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CameraFragmentBinding.inflate(
            inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Check camera permissions first
        if (isCameraPermissionGranted())
            binding.viewFinder.post {
                setupCameraX()
            }
        else requestPermissions(
            arrayOf(CAMERA), RC_PERMISSION)
    }

    private fun setupCameraX() {
        val executor = ContextCompat
            .getMainExecutor(requireContext())

        val cameraProviderFuture =
            ProcessCameraProvider
                .getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Get camera provider to bind use cases
            val cameraProvider =
                cameraProviderFuture.get()

            // Create a preview use case
            val preview = Preview
                .Builder()
                .build().apply {
                    val surfaceProvider = binding
                        .viewFinder.surfaceProvider
                    setSurfaceProvider(surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector
                .DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview)
            } catch(e: Exception) {
                Timber.e(e, "Use case binding failed")
            }
        }, executor)
    }

    private fun isCameraPermissionGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(), CAMERA) == PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == RC_PERMISSION &&
            isCameraPermissionGranted())
            setupCameraX()
        else {
            Snackbar.make(
                binding.root,
                getString(R.string.msg_camera_denied),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        private const val RC_PERMISSION = 1123
    }

}

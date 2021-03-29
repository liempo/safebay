package wtf.liempo.safebay.ui.home

import android.Manifest.permission.CAMERA
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import wtf.liempo.safebay.R
import wtf.liempo.safebay.utils.CameraUtils.getCameraProvider
import wtf.liempo.safebay.databinding.FragmentHomeScanBinding

class HomeScanFragment : Fragment() {

    private val vm: HomeViewModel by activityViewModels()
    private var _binding: FragmentHomeScanBinding? = null
    private val binding get() = _binding!!

    // Create the barcode analyzer here
    private val analyzer = BarcodeAnalyzer()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeScanBinding.inflate(
            inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        // Setup the analyzer once view created
        analyzer.setResultListener {
            vm.detectProfile(it)
        }

        vm.detected.observe(viewLifecycleOwner, {
            // Pause the analyzer if a profile is detected
            analyzer.isPaused = it != null

            // Show Confirm dialog
            findNavController().navigate(
                R.id.action_confirm_scan)
        })

        // Check camera permissions first
        if (isCameraPermissionGranted())
                setupCameraX()
        else requestPermissions(
            arrayOf(CAMERA), RC_PERMISSION
        )
    }

    private fun setupCameraX() {
        Timber.d("Setting up the camera")

        lifecycle.coroutineScope.launchWhenResumed {
            // Use main executor for camera
            // initialization and image analysis
            val executor = ContextCompat
                .getMainExecutor(requireContext())

            // Get camera provider (coroutine-ly)
            val provider = getCameraProvider(
                requireContext(), executor)

            // Select back camera as a default
            val selector = CameraSelector
                .DEFAULT_BACK_CAMERA

            // Create a preview use case
            val preview = Preview
                .Builder()
                .build()
                .apply {
                    val surfaceProvider = binding
                        .viewFinder.surfaceProvider
                    setSurfaceProvider(surfaceProvider)
                }

            val analysis = ImageAnalysis
                .Builder()
                .build().apply {
                    setAnalyzer(executor, analyzer)
                }

            try {
                // Unbind use cases before rebinding
                provider.unbindAll()

                // Bind use cases to camera
                provider.bindToLifecycle(
                    this@HomeScanFragment,
                    selector, preview, analysis)
            } catch(e: Exception) { Timber.e(e) }
        }
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
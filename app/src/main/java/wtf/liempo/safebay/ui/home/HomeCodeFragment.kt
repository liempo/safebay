package wtf.liempo.safebay.ui.home

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import wtf.liempo.safebay.R
import wtf.liempo.safebay.databinding.FragmentHomeCodeBinding
import wtf.liempo.safebay.utils.QRGenerator

class HomeCodeFragment : Fragment() {

    private val vm: HomeViewModel by activityViewModels()
    private var _binding: FragmentHomeCodeBinding? = null
    private val binding get() = _binding!!

    private lateinit var qr: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeCodeBinding.inflate(
            inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Return if currentUserId is null
        // (note that code wouldn't reach here if currentUserId is null)
        if (vm.currentUserId.isNullOrEmpty())
            return

        // Generate the QR bitmap from currentUserId
        qr = QRGenerator.generate(
            vm.currentUserId!!) ?: return

        Glide.with(binding.root)
            .load(qr)
            .into(binding.imageQr)

        binding.buttonSaveQr.setOnClickListener {
            // Save the QR code and get status
            val status = saveQrToGallery()

            // Create message based on status
            val msg = getString(
                if (status)
                    R.string.msg_success_qr_save
                else R.string.msg_error_qr_save
            )

            // Show msg in UI
            Snackbar.make(
                binding.root,
                msg,
                Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun saveQrToGallery(): Boolean {
        // Get the contentResolver
        val resolver = requireContext()
            .applicationContext
            .contentResolver

        // Find all image files on the primary external storage device.
        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        // Create the details for the media we are saving
        val details = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME,
                "Safebay_${vm.currentUserId}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        // Insert a reference and write later
        val uri = resolver.insert(
            collection, details) ?: return false

        // Write data into reference
        resolver.openFileDescriptor(uri, "w").use {
            val out = ParcelFileDescriptor
                .AutoCloseOutputStream(it)
            qr.compress(Bitmap.CompressFormat.JPEG,
                100, out)
        }

        // Now that we're finished, release the "pending" statusa
        details.clear()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            details.put(MediaStore.Audio.Media.IS_PENDING, 0)
        }

        resolver.update(uri, details,
            null, null)

        return true
    }

}

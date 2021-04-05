package wtf.liempo.safebay.ui.home

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import wtf.liempo.safebay.databinding.FragmentHomeSettingsBinding

@Suppress("DEPRECATION")
class HomeSettingsFragment : Fragment() {

    private val vm: HomeViewModel by activityViewModels()
    private var _binding: FragmentHomeSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeSettingsBinding.inflate(
            inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = requireActivity()
            .getPreferences(Context.MODE_PRIVATE)
        binding.profile.setType(vm.getType(pref))

        // We gotta implement the image chooser here
        // because there's no way a custom view can
        // intercept a startActivityForResult
        binding.profile.setProfileImageClickListener {
            val intent = Intent(Intent.ACTION_PICK)
                .apply { type = "image/*" }
            startActivityForResult(intent, RC_PICKER)
        }

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) return

        // Extract Uri else return if null
        val uri = (data?.data as Uri).toString()

        // Let's set the profile image to this
        binding.profile.imageUri = uri
    }

    companion object {
        private const val RC_PICKER = 329
    }
}

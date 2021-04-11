package wtf.liempo.safebay.ui.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.dhaval2404.imagepicker.ImagePicker
import wtf.liempo.safebay.databinding.FragmentAuthProfileBinding

@Suppress("DEPRECATION")
class AuthProfileFragment : Fragment() {

    private val vm: AuthViewModel by activityViewModels()
    private var _binding: FragmentAuthProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthProfileBinding.inflate(
            inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = requireActivity()
            .getPreferences(Context.MODE_PRIVATE)
        binding.profile.setType(vm.getType(pref))
        binding.progressBar.hide()

        // We gotta implement the image chooser here
        // because there's no way a custom view can
        // intercept a startActivityForResult
        binding.profile.setProfileImageClickListener {
            ImagePicker.Companion.with(this)
                .cameraOnly()
                .start()
        }

        binding.buttonSignUp.setOnClickListener {
            val profile = binding.profile.toProfile() ?:
                return@setOnClickListener

            binding.profile.setEditEnabled(false)
            binding.buttonSignUp.isEnabled = false
            binding.progressBar.show()

            vm.startProfileCreate(profile)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK)
            return
        binding.profile.imageUri = data?.data.toString()
    }

}

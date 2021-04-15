package wtf.liempo.safebay.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import wtf.liempo.safebay.R
import wtf.liempo.safebay.databinding.FragmentHomeManualBinding

class HomeManualFragment : Fragment() {

    private val vm: HomeViewModel by activityViewModels()
    private var _binding: FragmentHomeManualBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeManualBinding.inflate(
            inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = requireActivity()
            .getPreferences(Context.MODE_PRIVATE)
        binding.profile.setType(vm.getType(pref))
        binding.progressBar.hide()

        binding.profile.setProfileImageClickListener {
            ImagePicker.Companion.with(this)
                .start()
        }

        binding.buttonDone.setOnClickListener {
            val profile = binding.profile.toProfile() ?:
                return@setOnClickListener

            binding.profile.setEditEnabled(false)
            binding.buttonDone.isEnabled = false
            binding.progressBar.show()

            vm.startProfileCreateAndLog(profile)
        }

        vm.logged.observe(viewLifecycleOwner, {
            if (it) {
                binding.profile.clear()
                binding.profile.setEditEnabled(true)
                binding.buttonDone.isEnabled = true
                binding.progressBar.hide()

                Snackbar.make(binding.root,
                    R.string.msg_log_done,
                    Snackbar.LENGTH_SHORT)
                    .show()
            }
        })
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

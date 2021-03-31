package wtf.liempo.safebay.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import wtf.liempo.safebay.R
import wtf.liempo.safebay.databinding.FragmentHomeConfirmBinding
import wtf.liempo.safebay.models.Profile

class HomeConfirmFragment : DialogFragment() {

    private val vm: HomeViewModel by activityViewModels()
    private var _binding: FragmentHomeConfirmBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeConfirmBinding.inflate(
            inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Extract profile from args
        val profile: Profile? = arguments?.
            getParcelable("profile")

        // Update fields with profile
        profile?.let {
            Glide.with(binding.root)
                .load(profile.imageUri)
                .into(binding.imageProfile)
            binding.textName.text = profile.name
            binding.textAddress.text =
                profile.address.toString()
        }

        // Initially hide progress bar and main content
        binding.progress.hide()

        // Create onClicks for buttons
        binding.buttonConfirm
            .setOnClickListener {
                vm.startLogProfile()
            }
        binding.buttonCancel
            .setOnClickListener {
                close()
            }

        vm.logged.observe(viewLifecycleOwner, {
            if (!it) { // Log starting
                binding.buttons.visibility = View.GONE
                binding.progress.show()
                binding.textMessage.text = getString(
                    R.string.msg_log_loading)
            } else {
                binding.progress.hide()
                binding.textMessage.text = getString(
                    R.string.msg_log_done)
            }
        })
    }

    /** Close this fragment, clear any detected profile */
    private fun close() { vm.clearDetectedProfile(); dismiss() }

}

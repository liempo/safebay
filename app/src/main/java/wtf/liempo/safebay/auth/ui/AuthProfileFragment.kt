package wtf.liempo.safebay.auth.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import wtf.liempo.safebay.databinding.FragmentAuthProfileBinding

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

        binding.buttonSignUp.setOnClickListener {
            val profile = binding.profile.toProfile() ?:
                return@setOnClickListener
            vm.startProfileCreate(profile)
        }
    }
}

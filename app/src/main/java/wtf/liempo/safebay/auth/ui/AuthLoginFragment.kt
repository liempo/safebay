package wtf.liempo.safebay.auth.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import wtf.liempo.safebay.auth.model.Type
import wtf.liempo.safebay.databinding.FragmentAuthLoginBinding

class AuthLoginFragment : Fragment() {

    private val vm: AuthViewModel by activityViewModels()
    private var _binding: FragmentAuthLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthLoginBinding.inflate(
            inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSignIn.setOnClickListener {
            vm.startAuth(requireActivity(), Type.STANDARD)
        }

        binding.buttonBusinessSignIn.setOnClickListener {
            vm.startAuth(requireActivity(), Type.BUSINESS)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode, resultCode, data)
        vm.onAuthResult(
            requestCode, resultCode, data)
    }

}

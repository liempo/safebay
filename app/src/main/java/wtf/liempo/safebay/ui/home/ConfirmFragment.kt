package wtf.liempo.safebay.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import wtf.liempo.safebay.R
import wtf.liempo.safebay.databinding.FragmentAuthStartBinding
import wtf.liempo.safebay.databinding.FragmentConfirmBinding
import wtf.liempo.safebay.ui.auth.AuthViewModel

class ConfirmFragment : DialogFragment() {

    private val vm: HomeViewModel by activityViewModels()
    private var _binding: FragmentConfirmBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmBinding.inflate(
            inflater, container, false)
        return binding.root
    }

}

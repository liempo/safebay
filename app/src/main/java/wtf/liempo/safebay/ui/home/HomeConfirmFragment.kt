package wtf.liempo.safebay.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import wtf.liempo.safebay.databinding.FragmentHomeConfirmBinding

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

}

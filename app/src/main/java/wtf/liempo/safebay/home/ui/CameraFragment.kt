package wtf.liempo.safebay.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import wtf.liempo.safebay.databinding.CameraFragmentBinding

class CameraFragment : Fragment() {

    private val vm: HomeViewModel by activityViewModels()
    private var _binding: CameraFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CameraFragmentBinding.inflate(
            inflater, container, false)
        return binding.root
    }

}

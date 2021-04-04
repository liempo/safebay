package wtf.liempo.safebay.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import wtf.liempo.safebay.databinding.FragmentHomeCodeBinding
import wtf.liempo.safebay.utils.QRGenerator

class HomeCodeFragment : Fragment() {

    private val vm: HomeViewModel by activityViewModels()
    private var _binding: FragmentHomeCodeBinding? = null
    private val binding get() = _binding!!

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

        vm.currentUserId?.let {
           val bitmap =  QRGenerator.generate(requireContext(), it)

            Glide.with(binding.root)
                .load(bitmap)
                .into(binding.imageQr)
        }
    }

}

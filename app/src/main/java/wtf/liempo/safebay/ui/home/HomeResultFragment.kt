package wtf.liempo.safebay.ui.home

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import timber.log.Timber
import wtf.liempo.safebay.R
import wtf.liempo.safebay.databinding.FragmentHomeResultBinding
import wtf.liempo.safebay.models.SymptomResult

class HomeResultFragment : DialogFragment() {

    private val vm: HomeViewModel by activityViewModels()
    private var _binding: FragmentHomeResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeResultBinding.inflate(
            inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Extract result from args
        val result: SymptomResult = arguments?.
            getSerializable("result") as SymptomResult
        Timber.d("Result: $result")

        val red = ContextCompat.getColor(
            requireContext(), R.color.red)
        val orange = ContextCompat.getColor(
            requireContext(), R.color.orange)
        val green = ContextCompat.getColor(
            requireContext(), R.color.green)

        when (result) {
            SymptomResult.LOW -> {
                binding.imageIcon.setImageResource(
                    R.drawable.ic_check_24)
                binding.imageIcon.setColorFilter(green)
                binding.textMessage.text = getString(
                    R.string.msg_symptoms_low)
            }
            SymptomResult.WARNING -> {
                binding.imageIcon.setImageResource(
                    R.drawable.ic_warning_24)
                binding.imageIcon.setColorFilter(orange)
                binding.textMessage.text = getString(
                    R.string.msg_symptoms_warning)
            }
            SymptomResult.CRITICAL -> {
                binding.imageIcon.setImageResource(
                    R.drawable.ic_warning_24)
                binding.imageIcon.setColorFilter(red)
                binding.textMessage.text = getString(
                    R.string.msg_symptoms_critical)
            }
        }

        Timber.d("Text = ${binding.textMessage.text}")
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        vm.clearDetectedProfile()
    }
}

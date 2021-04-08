package wtf.liempo.safebay.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import timber.log.Timber
import wtf.liempo.safebay.R
import wtf.liempo.safebay.databinding.FragmentHomeSymptomsBinding

class HomeSymptomsFragment : Fragment() {

    private val vm: HomeViewModel by activityViewModels()
    private var _binding: FragmentHomeSymptomsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SymptomsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeSymptomsBinding.inflate(
            inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SymptomsAdapter()

        adapter.setOnCompleteListener { l ->
            val result = vm.computeResult(l)

            findNavController().navigate(
                R.id.action_symptoms_result,
                bundleOf("result" to result)
            )
        }

        with (binding.rvSymptoms) {
            adapter = this@HomeSymptomsFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }

        vm.startSymptomsFetch()
        vm.listSymptoms.observe(viewLifecycleOwner, {
            Timber.d("Received")
            adapter.items = it
            adapter.notifyDataSetChanged()
        })
    }
}

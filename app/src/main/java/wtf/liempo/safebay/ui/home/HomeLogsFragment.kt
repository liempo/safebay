package wtf.liempo.safebay.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import wtf.liempo.safebay.R
import wtf.liempo.safebay.databinding.FragmentHomeLogsBinding
import java.text.SimpleDateFormat
import java.util.*


class HomeLogsFragment : Fragment() {

    private val vm: HomeViewModel by activityViewModels()
    private var _binding: FragmentHomeLogsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: LogsAdapter

    private val picker by lazy {
        MaterialDatePicker.Builder
            .dateRangePicker()
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeLogsBinding.inflate(
            inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view, savedInstanceState)


        adapter = LogsAdapter()

        with (binding.rvLogs) {
            adapter = this@HomeLogsFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }

        val pref = requireActivity().
            getSharedPreferences(
                getString(R.string.app_name),
                Context.MODE_PRIVATE)
        val type = vm.getType(pref)

        vm.startLogsFetch(type)
        vm.listLogs.observe(viewLifecycleOwner, {
            adapter.items = it
            adapter.notifyDataSetChanged()
        })

        picker.addOnNegativeButtonClickListener {
            binding.buttonFilter.text =
                getString(R.string.action_filter)
            vm.startLogsFetch(type)
        }

        picker.addOnPositiveButtonClickListener {
            // Convert to date object
            val start = Date(it.first!!)
            val end = Date(it.second!!)

            // Format date
            val format = SimpleDateFormat(
                "MMM dd", Locale.getDefault())

            binding.buttonFilter.text = getString(
                R.string.format_date_range,
                format.format(start),
                format.format(end))

            vm.filterLogs(start, end)
        }

        binding.buttonFilter.setOnClickListener {
            picker.show(parentFragmentManager, picker.toString())
        }
    }

}

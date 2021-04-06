package wtf.liempo.safebay.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import wtf.liempo.safebay.databinding.FragmentHomeLogsBinding


class HomeLogsFragment : Fragment() {

    private val vm: HomeViewModel by activityViewModels()
    private var _binding: FragmentHomeLogsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: LogsAdapter

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
        super.onViewCreated(view, savedInstanceState)

        adapter = LogsAdapter()

        with (binding.rvLogs) {
            adapter = this@HomeLogsFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }

        val pref = requireActivity()
            .getPreferences(Context.MODE_PRIVATE)
        val type = vm.getType(pref)

        vm.startLogsFetch(type)
        vm.list.observe(viewLifecycleOwner, {
            adapter.items = it
        })

    }


}

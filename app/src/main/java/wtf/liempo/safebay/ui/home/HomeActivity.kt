package wtf.liempo.safebay.ui.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomappbar.BottomAppBar.*
import timber.log.Timber
import wtf.liempo.safebay.R
import wtf.liempo.safebay.databinding.ActivityHomeBinding
import wtf.liempo.safebay.models.HomeState
import wtf.liempo.safebay.models.Type

class HomeActivity : AppCompatActivity() {

    private val vm: HomeViewModel by  viewModels()
    private lateinit var controller: NavController
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up view binding
        binding = ActivityHomeBinding
            .inflate(layoutInflater)
        setContentView(binding.root)

        // Get navController first
        controller = (supportFragmentManager
            .findFragmentById(R.id.container)
                as NavHostFragment)
            .navController

        binding.fab.setOnClickListener {
            vm.setPrimaryState()
        }

        binding.bar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_settings -> vm.setState(
                    HomeState.SETTINGS)
                R.id.menu_logs -> vm.setState(
                    HomeState.LOGS
                )
            }

            true
        }

        // Get shared preferences
        val pref = getSharedPreferences(
                getString(R.string.app_name),
                Context.MODE_PRIVATE)
        val type = vm.getType(pref)

        vm.state.observe(this, {
            Timber.d("State: $it")

            // Change fab icon based on state
            val drawable = ContextCompat.getDrawable(this,
                if (it == HomeState.SCAN)
                    R.drawable.ic_help_24
                else R.drawable.ic_qr_24
            ); binding.fab.setImageDrawable(drawable)

            when (it) {
                HomeState.SCAN -> {
                    // Change fragment based on type
                    val actionId = when (type) {
                        Type.STANDARD -> R.id.action_to_scan
                        Type.BUSINESS -> R.id.action_to_code
                    }; controller.navigate(actionId)
                }

                HomeState.HELP ->
                    controller.navigate(R.id.action_to_symptoms)
                HomeState.LOGS ->
                    controller.navigate(R.id.action_to_logs)
                HomeState.SETTINGS ->
                    controller.navigate(R.id.action_to_settings)

                else -> return@observe
            }
        })
    }
}

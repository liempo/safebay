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

        vm.state.observe(this, {
            Timber.d("State: $it")

            when (it) {
                HomeState.SCAN -> {
                    // Change fab icon to help
                    binding.fab.setImageDrawable(
                        ContextCompat.getDrawable(
                            this, R.drawable.ic_help_24))

                    // Get shared preferences
                    val pref = getPreferences(
                        Context.MODE_PRIVATE)

                    // Change fragment based on type
                    val actionId = when (vm.getType(pref)) {
                        Type.STANDARD -> R.id.action_to_scan
                        Type.BUSINESS -> R.id.action_to_code
                    }; controller.navigate(actionId)
                }

                HomeState.LIST -> TODO()
                HomeState.SETTINGS -> TODO()

                else -> return@observe
            }
        })
    }
}

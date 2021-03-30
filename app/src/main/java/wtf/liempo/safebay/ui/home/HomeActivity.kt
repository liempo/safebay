package wtf.liempo.safebay.ui.home

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
            vm.setState(HomeState.SCAN)
        }

        vm.state.observe(this, {
            Timber.d("State: $it")

            when (it) {
                HomeState.SCAN -> {
                    // Change fab icon to help
                    binding.fab.setImageDrawable(
                        ContextCompat.getDrawable(
                            this, R.drawable.ic_help_24))

                    // Change fragment
                    controller.navigate(
                        R.id.to_scan)
                }

                HomeState.LIST -> TODO()

                else -> return@observe
            }
        })

    }
}

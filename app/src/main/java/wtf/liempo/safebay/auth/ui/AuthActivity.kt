package wtf.liempo.safebay.auth.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import timber.log.Timber
import wtf.liempo.safebay.R
import wtf.liempo.safebay.auth.model.Phase
import wtf.liempo.safebay.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private val vm: AuthViewModel by viewModels()
    private lateinit var controller: NavController
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())

        // Set up view binding
        binding = ActivityAuthBinding
            .inflate(layoutInflater)
        setContentView(binding.root)

        // Get navController first
        controller = (supportFragmentManager
            .findFragmentById(R.id.container)
                as NavHostFragment)
            .navController

        // Start the authentication flow
        vm.startPhaseCheck()

        // Set up the view model
        vm.phase.observe(this, {
            Timber.d("Phase: $it")

            val actionId = when (it) {
                Phase.LOGIN -> R.id.to_login
                Phase.PROFILE -> R.id.to_profile
                Phase.FINISH ->  R.id.to_home
                else -> return@observe
            }; controller.navigate(actionId)
        })
    }
}

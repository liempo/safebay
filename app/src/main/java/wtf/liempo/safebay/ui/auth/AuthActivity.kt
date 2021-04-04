package wtf.liempo.safebay.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import timber.log.Timber
import wtf.liempo.safebay.R
import wtf.liempo.safebay.models.AuthState
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
        vm.startStateCheck()

        // Set up the view model
        vm.state.observe(this, {
            Timber.d("Phase: $it")

            val actionId = when (it) {
                AuthState.LOGIN -> R.id.action_to_login
                AuthState.PROFILE -> R.id.action_to_profile
                AuthState.FINISH ->  R.id.action_to_home
                else -> return@observe
            }; controller.navigate(actionId)
        })
    }
}

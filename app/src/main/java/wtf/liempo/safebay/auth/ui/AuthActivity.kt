package wtf.liempo.safebay.auth.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
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
        controller = binding.root
            .findNavController()

        // Set up the view model
        vm.phase.observe(this, {
            val actionId = when (it) {
                Phase.LOGIN -> R.id.action_start_to_login
                Phase.PROFILE -> R.id.action_start_to_login
                Phase.FINISH -> R.id.action_start_to_login
                else -> return@observe
            }; controller.navigate(actionId)
        })
    }
}

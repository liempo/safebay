package wtf.liempo.safebay.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import timber.log.Timber
import wtf.liempo.safebay.R
import wtf.liempo.safebay.models.HomeState

class HomeActivity : AppCompatActivity() {

    private val vm: HomeViewModel by  viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Get navController first
        val controller = (supportFragmentManager
            .findFragmentById(R.id.container)
                as NavHostFragment)
            .navController

        vm.state.observe(this, {
            val actionId = when (it) {
                HomeState.CONFIRM -> R.id.action_camera_to_confirm
                else -> R.id.action_camera_to_confirm
            }; controller.navigate(actionId)
        })

    }
}

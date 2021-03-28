package wtf.liempo.safebay.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import timber.log.Timber
import wtf.liempo.safebay.R

class HomeActivity : AppCompatActivity() {

    private val vm: HomeViewModel by  viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        vm.detected.observe(this, {
            if (it == null)
                return@observe
            Timber.i("Retrieved profile: ${it.name}")
        })
    }
}

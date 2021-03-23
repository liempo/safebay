package wtf.liempo.safebay.auth.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.firebase.ui.auth.AuthUI
import wtf.liempo.safebay.R

class AuthActivity : AppCompatActivity() {

    private val vm: AuthViewModel by viewModels()

    private val provider by lazy {
        AuthUI.IdpConfig.PhoneBuilder()
                .setWhitelistedCountries(listOf("PH"))
                .build()
    }

    private val auth by lazy {
        AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(listOf(provider))
                .setDefaultProvider(provider)
                .setTheme(R.style.Theme_Safebay)
                .setLogo(R.drawable.banner_app)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }

    companion object {
        private const val RC_AUTH = 42069
    }
}

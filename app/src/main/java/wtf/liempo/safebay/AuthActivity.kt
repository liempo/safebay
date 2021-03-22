package wtf.liempo.safebay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.auth.AuthUI

class AuthActivity : AppCompatActivity() {

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
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        startActivityForResult(auth, RC_AUTH)
    }

    companion object {
        private const val RC_AUTH = 42069
    }
}

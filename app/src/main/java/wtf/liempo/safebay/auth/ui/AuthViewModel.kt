package wtf.liempo.safebay.auth.ui

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import kotlinx.coroutines.launch
import timber.log.Timber
import wtf.liempo.safebay.R
import wtf.liempo.safebay.auth.data.AuthRepository
import wtf.liempo.safebay.auth.model.Phase
import wtf.liempo.safebay.auth.model.Type

class AuthViewModel : ViewModel() {

    private val repo = AuthRepository()

    // Determines the state of the authentication
    private val _phase = MutableLiveData<Phase>()
    val phase: LiveData<Phase> = _phase

    // Determines the type of the signed in user
    private val _type = MutableLiveData<Type>()
    val type: LiveData<Type> = _type

    init { startPhaseCheck() }

    private fun startPhaseCheck() {
        viewModelScope.launch {
            _phase.value =
                if (repo.getCurrentUserId().isNullOrEmpty()) {
                    if (repo.getCurrentProfile() != null)
                        Phase.FINISH
                    else Phase.PROFILE
                } else Phase.LOGIN
        }
    }

    private fun startProfileCheck() {
        viewModelScope.launch {
            _phase.value =
                if (repo.getCurrentProfile() != null)
                    Phase.FINISH
                else Phase.PROFILE
        }
    }

    /** Shows the firebase UI authentication screen */
    fun startAuth(activity: FragmentActivity, startAsType: Type) {
        val provider = AuthUI.IdpConfig.PhoneBuilder()
            .setWhitelistedCountries(listOf("PH"))
            .build()

        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(listOf(provider))
            .setDefaultProvider(provider)
            .setTheme(R.style.Theme_Safebay)
            .setLogo(R.drawable.banner_app)
            .build()

        _type.value = startAsType
        activity.startActivityForResult(
            intent, RC_AUTH)
    }

    fun onAuthResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode != RC_AUTH)
            return
        val response = IdpResponse
            .fromResultIntent(data)
        if (resultCode == Activity.RESULT_OK)
            startProfileCheck()
        else Timber.e(response?.error)
    }

    companion object {
        private const val RC_AUTH = 42069
    }
}

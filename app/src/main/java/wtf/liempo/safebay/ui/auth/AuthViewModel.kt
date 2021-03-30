package wtf.liempo.safebay.ui.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import wtf.liempo.safebay.data.ImageRepository
import wtf.liempo.safebay.models.AuthState
import wtf.liempo.safebay.models.Type
import wtf.liempo.safebay.models.Profile
import wtf.liempo.safebay.data.ProfileRepository

class AuthViewModel : ViewModel() {

    // Data Repositories
    private val profiles = ProfileRepository()
    private val images = ImageRepository()

    // Non-observable data, used internally
    private val uid: String?
        get() = profiles.currentUserId

    // Determines the state of the authentication
    private val _state = MutableLiveData<AuthState>()
    val state: LiveData<AuthState> = _state

    // Notify the the UI if some elements are loading
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun startStateCheck() {
        viewModelScope.launch {
            _state.value =
                if (!uid.isNullOrEmpty()) {
                    if (profiles.getCurrentProfile() != null)
                        AuthState.FINISH
                    else AuthState.PROFILE
                } else AuthState.LOGIN
        }
    }

    fun startProfileCheck() {
        viewModelScope.launch {
            _state.value =
                if (profiles.getCurrentProfile() != null)
                    AuthState.FINISH
                else AuthState.PROFILE
        }
    }

    fun startProfileCreate(profile: Profile) {
        viewModelScope.launch {
            // Upload image to storage
            // NOTE: I explicitly put a not null here
            // because it at this point we have verified
            // that uid is not null on startProfileCheck
            // and imageUri must be verified before calling
            // startProfileCheck. Thank you, have a good day!
            val imageUri = images.uploadProfileImage(
                uid!!, profile.imageUri!!)

            val updated = profile.copy(
                imageUri = imageUri)

            _state.value =
                if (profiles.setCurrentProfile(updated))
                    AuthState.FINISH
                else AuthState.PROFILE
        }
    }

    fun setType(pref: SharedPreferences, type: Type) {
        pref.edit { putString(PREF_KEY_TYPE, type.toString()) }
    }

    fun getType(pref: SharedPreferences): Type {
        val value = pref.getString(PREF_KEY_TYPE, null)
            ?: return Type.STANDARD
        return Type.valueOf(value)
    }

    companion object {
        private const val PREF_KEY_TYPE = "auth_type"
    }

}

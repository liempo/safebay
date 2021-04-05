package wtf.liempo.safebay.ui.auth

import android.content.SharedPreferences
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
import wtf.liempo.safebay.data.TypeRepository

class AuthViewModel : ViewModel() {

    // Data Repositories
    private val profiles = ProfileRepository()
    private val images = ImageRepository()
    private val types = TypeRepository()

    // Non-observable data, used internally
    private val currentUserId: String?
        get() = profiles.getCurrentUserId()

    // Determines the state of the authentication
    private val _state = MutableLiveData<AuthState>()
    val state: LiveData<AuthState> = _state

    fun startStateCheck() {
        viewModelScope.launch {
            _state.value =
                if (!currentUserId.isNullOrEmpty()) {
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
                currentUserId!!, profile.imageUri!!)

            val updated = profile.copy(
                imageUri = imageUri)

            _state.value =
                if (profiles.setCurrentProfile(updated))
                    AuthState.FINISH
                else AuthState.PROFILE
        }
    }

    fun setType(pref: SharedPreferences, type: Type) =
        types.setType(pref, type)

    fun getType(pref: SharedPreferences): Type =
        types.getType(pref)

}

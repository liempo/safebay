package wtf.liempo.safebay.ui.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import wtf.liempo.safebay.data.ImageRepository
import wtf.liempo.safebay.models.Phase
import wtf.liempo.safebay.models.Type
import wtf.liempo.safebay.models.Profile
import wtf.liempo.safebay.data.ProfileRepository

class AuthViewModel : ViewModel() {

    private val profiles = ProfileRepository()
    private val images = ImageRepository()

    private val uid = profiles.getCurrentUserId()

    // Determines the state of the authentication
    private val _phase = MutableLiveData<Phase>()
    val phase: LiveData<Phase> = _phase

    fun startPhaseCheck() {
        viewModelScope.launch {
            _phase.value =
                if (!uid.isNullOrEmpty()) {
                    if (profiles.getCurrentProfile() != null)
                        Phase.FINISH
                    else Phase.PROFILE
                } else Phase.LOGIN
        }
    }

    fun startProfileCheck() {
        viewModelScope.launch {
            _phase.value =
                if (profiles.getCurrentProfile() != null)
                    Phase.FINISH
                else Phase.PROFILE
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
                profiles.getCurrentUserId()!!,
                profile.imageUri!!)

            val updated = profile.copy(
                imageUri = imageUri)

            _phase.value =
                if (profiles.setCurrentProfile(updated))
                    Phase.FINISH
                else Phase.PROFILE
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

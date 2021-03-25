package wtf.liempo.safebay.auth.ui

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import wtf.liempo.safebay.auth.model.Phase
import wtf.liempo.safebay.common.models.Type
import wtf.liempo.safebay.common.models.Profile
import wtf.liempo.safebay.data.ProfileRepository

class AuthViewModel : ViewModel() {

    private val repo = ProfileRepository()

    // Determines the state of the authentication
    private val _phase = MutableLiveData<Phase>()
    val phase: LiveData<Phase> = _phase

    fun startPhaseCheck() {
        viewModelScope.launch {
            _phase.value =
                if (!repo.getCurrentUserId().isNullOrEmpty()) {
                    if (repo.getCurrentProfile() != null)
                        Phase.FINISH
                    else Phase.PROFILE
                } else Phase.LOGIN
        }
    }

    fun startProfileCheck() {
        viewModelScope.launch {
            _phase.value =
                if (repo.getCurrentProfile() != null)
                    Phase.FINISH
                else Phase.PROFILE
        }
    }

    fun startProfileCreate(profile: Profile) {
        viewModelScope.launch {
            _phase.value =
                if (repo.setCurrentProfile(profile))
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

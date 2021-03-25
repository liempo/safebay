package wtf.liempo.safebay.auth.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import wtf.liempo.safebay.auth.model.Phase
import wtf.liempo.safebay.auth.model.Type
import wtf.liempo.safebay.common.models.Profile
import wtf.liempo.safebay.data.ProfileRepository

class AuthViewModel : ViewModel() {

    private val repo = ProfileRepository()

    // Determines the state of the authentication
    private val _phase = MutableLiveData<Phase>()
    val phase: LiveData<Phase> = _phase

    // Determines the type of the signed in user
    private val _type = MutableLiveData<Type>()
    val type: LiveData<Type> = _type

    fun setAuthType(type: Type) {
        _type.value = type
    }

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

}

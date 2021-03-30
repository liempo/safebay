package wtf.liempo.safebay.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import wtf.liempo.safebay.models.Profile
import wtf.liempo.safebay.data.ProfileRepository
import wtf.liempo.safebay.models.HomeState

class HomeViewModel : ViewModel() {

    // Data Repositories
    private val profiles = ProfileRepository()

    // Determines the state of the home activity
    private val _state = MutableLiveData<HomeState>()
        .apply { value = HomeState.SCAN }
    val state: LiveData<HomeState> = _state

    private val _detected = MutableLiveData<Profile?>()
    val detected: LiveData<Profile?> = _detected

    fun setState(state: HomeState) {
        _state.value = state
    }

    suspend fun detectProfile(barcode: String) {
        // Use postValue instead of setValue
        // because this function will run
        // on background threads
        _detected.postValue(
            profiles.getProfile(barcode))
    }

    fun clearDetectedProfile() {
        _detected.value = null
    }

}

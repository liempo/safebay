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

    /** This function will be called
     *  by the main button (fab),
     *  sets the primary state of Home */
    fun setPrimaryState() {
        _state.value = when (_state.value) {
            // Swap primary state only
            HomeState.SCAN -> HomeState.HELP
            HomeState.HELP -> HomeState.SCAN

            // If coming from any other state
            else -> HomeState.SCAN // default
        }
    }

    /** Generic function for switching state,
     *  Will be called by onClickListeners */
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

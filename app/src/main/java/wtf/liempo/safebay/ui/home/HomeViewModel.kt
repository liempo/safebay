package wtf.liempo.safebay.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import wtf.liempo.safebay.data.LogRepository
import wtf.liempo.safebay.models.Profile
import wtf.liempo.safebay.data.ProfileRepository
import wtf.liempo.safebay.models.HomeState
import wtf.liempo.safebay.models.Log
import wtf.liempo.safebay.utils.SingleLiveEvent

class HomeViewModel : ViewModel() {

    // Data Repositories
    private val profiles = ProfileRepository()
    private val logs = LogRepository()

    // Non-observable data, used internally
    private val guestId = profiles.currentUserId
    private var businessId: String? = null

    // Determines the state of the home activity
    private val _state = MutableLiveData<HomeState>()
        .apply { value = HomeState.SCAN }
    val state: LiveData<HomeState> = _state

    // Initially null and when something is scanned
    // using detectProfile(), result will be saved here
    private val _detected = MutableLiveData<Profile?>()
    val detected: LiveData<Profile?> = _detected

    // Will be used to send notifications to activity
    private val _notification = SingleLiveEvent<String>()
    val notification: LiveData<String> = _notification

    // Will be used to notify UI that information is Logged
    private val _logged = MutableLiveData<Boolean>()
    val logged: LiveData<Boolean> = _logged

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
        // Fetch the profile, return if null
        val profile = profiles
            .getProfile(barcode) ?: return

        // Use postValue instead of setValue
        // because this function will run
        // on background threads
        _detected.postValue(profile)

        // Update the businessId of the profile
        // To be used later for startLogProfile
        businessId = barcode
    }

    fun clearDetectedProfile() {
        _detected.value = null
        businessId = ""
    }

    fun startLogProfile() {
        // Notify UI first
        _logged.value = false

        // This should not happen because
        // startLogProfile can only be called
        // if detectProfile successfully finished
        if (businessId.isNullOrEmpty())
            return

        viewModelScope.launch {
            // Create the log data
            // and save to repository
            val data = Log(
                guestId, businessId)
            logs.saveLog(data)

            _logged.value = true
        }
    }

}

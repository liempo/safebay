package wtf.liempo.safebay.ui.home

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import timber.log.Timber
import wtf.liempo.safebay.data.*
import wtf.liempo.safebay.models.*
import wtf.liempo.safebay.utils.SingleLiveEvent

class HomeViewModel : ViewModel() {

    // Data Repositories
    private val profiles = ProfileRepository()
    private val images = ImageRepository()
    private val logs = LogRepository()
    private val types = TypeRepository()
    private val symptoms = SymptomRepository()

    // Non-observable data, used internally
    val currentUserId: String?
        get() = profiles.getCurrentUserId()
    private var scannedId: String? = null
    private var processBarcode = true

    // Determines the state of the home activity
    private val _state = MutableLiveData<HomeState>()
        .apply { value = HomeState.SCAN }
    val state: LiveData<HomeState> = _state

    // Initially null and when something is scanned
    // using detectProfile(), result will be saved here
    private val _detected = SingleLiveEvent<Profile>()
    val detected: LiveData<Profile> = _detected

    // Will be used to send notifications to activity
    private val _notification = SingleLiveEvent<String>()
    val notification: LiveData<String> = _notification

    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> = _profile

    private val _listLogs = MutableLiveData<List<LogUnwrapped>>()
    val listLogs: LiveData<List<LogUnwrapped>> = _listLogs

    private val _listSymptoms = MutableLiveData<List<Symptom>>()
    val listSymptoms: LiveData<List<Symptom>> = _listSymptoms

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
        // Close if barcode should not be processed
        if (!processBarcode) return

        // Fetch the profile, return if null
        val profile = profiles
            .getProfile(barcode) ?: return

        // Use postValue instead of setValue
        // because this function will run
        // on background threads
        _detected.postValue(profile)
        processBarcode = false

        // Update the businessId of the profile
        // To be used later for startLogProfile
        scannedId = barcode
    }

    fun clearDetectedProfile() {
        processBarcode = true
        scannedId = ""
    }

    fun startLogProfile() {
        // Notify UI first
        _logged.value = false

        // This should not happen because
        // startLogProfile can only be called
        // if detectProfile successfully finished
        if (scannedId.isNullOrEmpty())
            return

        viewModelScope.launch {
            // Create the log data
            // and save to repository
            val data = Log(
                currentUserId, scannedId)
            logs.saveLog(data)

            // Notify UI that logging done
            _logged.value = true

            // Reset profile detection
            clearDetectedProfile()
        }
    }

    fun startProfileUpdate(profile: Profile, skipImage: Boolean = false) {
        viewModelScope.launch {

            val imageUri =
                if (skipImage)
                    images.uploadProfileImage(
                        currentUserId!!,
                        profile.imageUri!!)
                else profile.imageUri!!

            val updated = profile.copy(
                imageUri = imageUri)

            profiles.setCurrentProfile(updated)
        }
    }

    fun startProfileFetch() {
        viewModelScope.launch {
            _profile.value = profiles.getCurrentProfile()
        }
    }

    fun getType(pref: SharedPreferences) =
        types.getType(pref)

    fun startLogsFetch(type: Type) {
        viewModelScope.launch {
            Timber.d("Fetching logs for $currentUserId as $type")

            val logs = logs.getLogs(type, currentUserId!!)
            Timber.d("Logs fetched: ${logs.size}")
            val unwrappedList = mutableListOf<LogUnwrapped>()

            for (log in logs) {
                Timber.d("Unwrapping Log $log")
                val date = log.date ?: continue

                // Unwrap
                val uid = when (type) {
                    Type.STANDARD -> log.businessId
                    Type.BUSINESS -> log.guestId
                } ?: continue

                // Get the profile
                val profile = profiles
                    .getProfile(uid) ?: continue
                val unwrapped = LogUnwrapped(
                    profile, date)
                unwrappedList.add(unwrapped)

                // Notify observers
                _listLogs.value = unwrappedList
            }
        }
    }

    fun startSymptomsFetch() {
        viewModelScope.launch {
            Timber.d("Fetching list of symptoms")
            _listSymptoms.value = symptoms.getSymptoms()
        }
    }
}

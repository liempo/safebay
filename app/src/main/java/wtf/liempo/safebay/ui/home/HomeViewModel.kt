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

    // Non-observable data, used internally
    private var profile: Profile? = null

    // Determines the state of the home activity
    private val _state = MutableLiveData<HomeState>()
    val state: LiveData<HomeState> = _state

    suspend fun searchProfile(barcode: String) {
        // Save the currently detected profile if not null
        profile = profiles.getProfile(barcode) ?: return
        _state.postValue(HomeState.CONFIRM)
    }

}

package wtf.liempo.safebay.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import wtf.liempo.safebay.models.Profile
import wtf.liempo.safebay.data.ProfileRepository

class HomeViewModel : ViewModel() {

    private val repo = ProfileRepository()

    // Contains the current profile detected with scanner
    private val _detected = MutableLiveData<Profile>()
    val detected: LiveData<Profile> = _detected

    suspend fun searchProfile(barcode: String) {
        // Use postValue instead of setValue
        // because this function will run
        // on background threads
        _detected.postValue(
            repo.getProfile(barcode))
    }

}

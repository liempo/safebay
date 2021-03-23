package wtf.liempo.safebay.auth.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import wtf.liempo.safebay.auth.model.Phase

class AuthViewModel : ViewModel() {

    private val _phase = MutableLiveData<Phase>()
    val phase: LiveData<Phase> = _phase

    init {
        if (Firebase)

    }

}

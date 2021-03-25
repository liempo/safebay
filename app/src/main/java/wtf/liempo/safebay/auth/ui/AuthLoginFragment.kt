package wtf.liempo.safebay.auth.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import timber.log.Timber
import wtf.liempo.safebay.R
import wtf.liempo.safebay.common.models.Type
import wtf.liempo.safebay.databinding.FragmentAuthLoginBinding

class AuthLoginFragment : Fragment() {

    private val vm: AuthViewModel by activityViewModels()
    private var _binding: FragmentAuthLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthLoginBinding.inflate(
            inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonStandardSignIn
            .setOnClickListener { startAuthUI(Type.STANDARD) }
        binding.buttonBusinessSignIn
            .setOnClickListener { startAuthUI(Type.BUSINESS) }
    }

    private fun startAuthUI(startAsType: Type) {
        val provider = AuthUI.IdpConfig.PhoneBuilder()
            .setWhitelistedCountries(listOf("PH"))
            .build()

        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(listOf(provider))
            .setDefaultProvider(provider)
            .setIsSmartLockEnabled(false)
            .setTheme(R.style.Theme_Safebay)
            .setLogo(R.drawable.banner_app)
            .build()

        val pref = requireActivity()
            .getPreferences(Context.MODE_PRIVATE)
        vm.setType(pref, startAsType)

        startActivityForResult(intent, RC_AUTH)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode, resultCode, data)

        if (requestCode != RC_AUTH)
            return
        val response = IdpResponse
            .fromResultIntent(data)
        if (resultCode == Activity.RESULT_OK)
            vm.startProfileCheck()
        else Timber.e(response?.error)
    }

    companion object {
        private const val RC_AUTH = 42069
    }
}

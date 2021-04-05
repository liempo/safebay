package wtf.liempo.safebay.ui.auth

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
import wtf.liempo.safebay.models.Type
import wtf.liempo.safebay.databinding.FragmentAuthLoginBinding
import wtf.liempo.safebay.models.AuthState

@Suppress("DEPRECATION")
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

        vm.state.observe(viewLifecycleOwner, {
            if (it == AuthState.LOGIN)
                binding.buttons.visibility = View.VISIBLE
        })

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
            .getSharedPreferences(
                getString(R.string.app_name),
                Context.MODE_PRIVATE)
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

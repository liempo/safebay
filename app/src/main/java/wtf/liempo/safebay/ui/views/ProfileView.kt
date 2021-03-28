package wtf.liempo.safebay.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputEditText
import wtf.liempo.safebay.R
import wtf.liempo.safebay.models.Address
import wtf.liempo.safebay.models.Profile
import wtf.liempo.safebay.databinding.ViewProfileBinding
import wtf.liempo.safebay.models.Type

class ProfileView : ConstraintLayout {

    private var _binding: ViewProfileBinding? = null
    private val binding get() = _binding!!

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyle: Int) : super(
        context, attrs, defStyle
    ) {
        inflate()
    }

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs) {
        inflate()
    }

    constructor(context: Context) : super(context) {
        inflate()
    }

    private fun inflate() {
        _binding = ViewProfileBinding.inflate(
            LayoutInflater.from(context),
            this, true)
    }

    fun setType(type: Type) {
        when(type) {
            Type.STANDARD -> {
                binding.titleDetails.text =
                    context.getString(R.string.title_details_standard)
                binding.titleAddress.text =
                    context.getString(R.string.title_address_standard)
            }
            Type.BUSINESS -> {
                binding.titleDetails.text =
                    context.getString(R.string.title_details_business)
                binding.titleAddress.text =
                    context.getString(R.string.title_address_business)
            }
        }
    }

    fun toProfile(): Profile? {
        // TODO Implement profile picture upload

        return try {
            val name = binding.inputName.getInput(true)
            val address = Address(
                binding.inputAddressLine1.getInput(true),
                binding.inputAddressLine2.getInput(),
                binding.inputBrgy.getInput(true),
                binding.inputCity.getInput(true),
                binding.inputProvince.getInput(true))

            Profile(name, null, address)
        } catch (e: RequiredFieldEmptyException) { null }
    }

    companion object {
        fun TextInputEditText.getInput(
            required: Boolean = false
        ): String {
            val input = text.toString().trim()

            if (input.isEmpty() && required) {
                error = "This field is required"
                throw RequiredFieldEmptyException()
            }

            return input
        }
    }

    class RequiredFieldEmptyException : Exception()
}

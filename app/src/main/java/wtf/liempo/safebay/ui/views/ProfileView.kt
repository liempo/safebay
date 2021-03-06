package wtf.liempo.safebay.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import wtf.liempo.safebay.R
import wtf.liempo.safebay.models.Address
import wtf.liempo.safebay.models.Profile
import wtf.liempo.safebay.databinding.ViewProfileBinding
import wtf.liempo.safebay.models.Type

class ProfileView : ConstraintLayout {

    private var _binding: ViewProfileBinding? = null
    private val binding get() = _binding!!

    private var _imageUri: String? = null

    private var _phone: String? = null
    var phone: String?
        get() =  _phone
        set(value) {
            _phone = value
            binding.inputPhone.setText(value)
            binding.inputPhone.isEnabled = false
        }

    var imageUri: String?
        get() = _imageUri
        set(value) {
            _imageUri = value
            Glide.with(context)
                .load(value)
                .into(binding.imageProfile)
        }

    private var originalBoxStrokeWidth: Int = 0
    private var profileImageOnClick: OnClickListener? = null
    private var isEditEnabled = true

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
        originalBoxStrokeWidth = binding
            .layoutName.boxStrokeWidth
        val color = binding
            .inputName.textColors
            .defaultColor
        setEditTextColor(
            color,
            binding.inputName,
            binding.inputPhone,
            binding.inputAge,
            binding.inputSex,
            binding.inputAddressLine1,
            binding.inputAddressLine2,
            binding.inputBrgy,
            binding.inputCity,
            binding.inputProvince
        )

        val adapter = ArrayAdapter(
            context,
            android.R.layout.select_dialog_item,
            listOf("Male", "Female")
        )
        binding.inputSex.inputType = 0
        binding.inputSex.setAdapter(adapter)

    }

    fun clear() {
        binding.imageProfile.
            setImageResource(R.drawable.ic_person_24)
        _imageUri = null; _phone = ""

        binding.inputName.setText("")
        binding.inputPhone.setText("")
        binding.inputAge.setText("")
        binding.inputSex.setText("")
        binding.inputAddressLine1.setText("")
        binding.inputAddressLine2.setText("")
        binding.inputBrgy.setText("")
        binding.inputCity.setText("")
        binding.inputProvince.setText("")
    }

    fun setEditEnabled(value: Boolean) {
        isEditEnabled = value
        binding.imageProfile
            .isClickable = value
        binding.imageProfile.setOnClickListener(
            if (value) profileImageOnClick else null
        )

        setLayoutEnabled(
            value,
            binding.layoutName,
            binding.layoutPhone,
            binding.layoutAge,
            binding.layoutSex,
            binding.layoutAddressLine1,
            binding.layoutAddressLine2,
            binding.layoutBrgy,
            binding.layoutCity,
            binding.layoutProvince
        )
    }

    private fun setLayoutEnabled(
        value: Boolean,
        vararg layouts: TextInputLayout
    ) {
        for (l in layouts) {
            l.isEnabled = value
            l.boxStrokeWidth =
                if (value)
                    originalBoxStrokeWidth
                else 0
        }
    }


    private fun setEditTextColor(
        color: Int,
        vararg editTexts: EditText
    ) {
        for (l in editTexts)
            l.setTextColor(color)
    }

    fun setType(type: Type) {
        when(type) {
            Type.STANDARD -> {
                binding.titleDetails.text =
                    context.getString(R.string.title_details_standard)
                binding.titleAddress.text =
                    context.getString(R.string.title_address_standard)
                binding.otherDetails
                    .visibility = View.VISIBLE
            }
            Type.BUSINESS -> {
                binding.titleDetails.text =
                    context.getString(R.string.title_details_business)
                binding.titleAddress.text =
                    context.getString(R.string.title_address_business)
                binding.otherDetails
                    .visibility = View.GONE
            }
        }
    }

    fun setProfileImageClickListener(l: OnClickListener) {
        profileImageOnClick = l
        binding.imageProfile.setOnClickListener(
            if (isEditEnabled) profileImageOnClick else null)
    }

    fun setProfile(profile: Profile) {
        // Set details
        imageUri = profile.imageUri
        binding.inputName.setText(profile.name)
        binding.inputPhone.setText(profile.phone)
        binding.inputAge.setText(profile.age.toString())
        binding.inputSex.setText(profile.sex)

        // Unwrap address
        val address = profile.address ?: return

        // Set address
        binding.inputAddressLine1.setText(address.line1)
        binding.inputAddressLine2.setText(address.line2)
        binding.inputBrgy.setText(address.brgy)
        binding.inputCity.setText(address.city)
        binding.inputProvince.setText(address.province)
    }

    fun toProfile(): Profile? {
        if (imageUri.isNullOrEmpty()) {
            Snackbar.make(
                this,
                R.string.msg_error_profile_image_empty,
                Snackbar.LENGTH_SHORT)
                .show()
            return null
        }

        return try {
            val name = binding.inputName.getInput(true)
            val phone = binding.inputPhone.getInput(true)
            val age = binding.inputAge.getInput(false).toInt()
            val sex = binding.inputSex.text.toString()
            val address = Address(
                binding.inputAddressLine1.getInput(true),
                binding.inputAddressLine2.getInput(),
                binding.inputBrgy.getInput(true),
                binding.inputCity.getInput(true),
                binding.inputProvince.getInput(true))

            Profile(name, phone, age, sex, imageUri, address)
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

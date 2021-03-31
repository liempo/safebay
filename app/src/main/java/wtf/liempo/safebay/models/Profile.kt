package wtf.liempo.safebay.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile (
    val name: String? = null,
    val imageUri: String? = null,
    val address: Address? = null
) : Parcelable

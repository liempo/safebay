package wtf.liempo.safebay.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val line1: String? = null,
    val line2: String? = null,
    val brgy: String? = null,
    val city: String? = null,
    val province: String? = null
) : Parcelable {
    override fun toString(): String {
        return "$brgy, $city, $province"
    }
}

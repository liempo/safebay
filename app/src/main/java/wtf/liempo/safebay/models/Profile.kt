package wtf.liempo.safebay.models


data class Profile (
    val name: String? = null,
    val phone: String? = null,
    val age: Int = 0,
    val sex: String? = null,
    val imageUri: String? = null,
    val address: Address? = null
)

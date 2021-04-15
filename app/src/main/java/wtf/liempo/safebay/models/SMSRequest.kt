package wtf.liempo.safebay.models

data class SMSRequest(
    val recipients: List<String>? = null,
    val message: String? = null
)

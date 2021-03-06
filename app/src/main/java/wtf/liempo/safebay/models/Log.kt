package wtf.liempo.safebay.models

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Log(
    val guestId: String? = null,
    val businessId: String? = null,
    @ServerTimestamp val date: Date? = null
)

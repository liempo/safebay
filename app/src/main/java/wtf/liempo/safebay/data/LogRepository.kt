package wtf.liempo.safebay.data

import com.google.firebase.FirebaseException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import wtf.liempo.safebay.models.Log
import wtf.liempo.safebay.models.Type
import java.lang.Exception

class LogRepository {

    private val collection = Firebase.firestore
        .collection("logs")

    suspend fun saveLog(log: Log): Boolean {
        return try {
            collection
                .document()
                .set(log)
                .await()
            true
        } catch (e: FirebaseException) { false }
    }

    suspend fun getLogs(type: Type, uid: String): List<Log> {
        return try {
            val filterAs = when(type) {
                Type.STANDARD -> "guestId"
                Type.BUSINESS -> "businessId"
            }

            collection
                .whereEqualTo(filterAs, uid)
                .get()
                .await()
                .toObjects(Log::class.java)
        } catch (e: Exception) { emptyList() }
    }

}

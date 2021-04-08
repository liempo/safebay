package wtf.liempo.safebay.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import wtf.liempo.safebay.models.Symptom

class SymptomRepository {

    private val collection = Firebase.firestore
        .collection("symptoms")

    suspend fun getSymptoms(): List<Symptom> {
        return try {
            collection
                .get()
                .await()
                .toObjects(Symptom::class.java)
        } catch (e: Exception) { emptyList() }
    }

}

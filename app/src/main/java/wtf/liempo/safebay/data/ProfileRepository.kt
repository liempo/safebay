package wtf.liempo.safebay.data

import com.google.firebase.FirebaseException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import wtf.liempo.safebay.models.Profile

class ProfileRepository {

    private val collection = Firebase.firestore
        .collection("profiles")

    val currentUserId =
        Firebase.auth.currentUser?.uid

    suspend fun getCurrentProfile(): Profile? =
        getProfile(currentUserId ?:  "")

    suspend fun getProfile(uid: String): Profile? {
        if (uid.isEmpty()) return null

        return try {
            collection
                .document(uid)
                .get()
                .await()
                .toObject<Profile>()
        } catch (e: FirebaseException) { null }
    }

    suspend fun setCurrentProfile(update: Profile): Boolean {
        val uid = currentUserId ?: return false

        return try {
            collection
                .document(uid)
                .set(update)
                .await()
            true
        } catch (e: FirebaseException) { false }
    }

}

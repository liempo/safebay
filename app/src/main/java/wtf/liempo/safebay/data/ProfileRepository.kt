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

    fun getCurrentUserId(): String? =
        Firebase.auth.currentUser?.uid

    suspend fun getCurrentProfile(): Profile? =
        getProfile(getCurrentUserId() ?:  "")

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

    suspend fun addAnonymousProfile(profile: Profile): String? {
        return try {
            val ref = collection
                .document()
            ref.set(profile)
                .await()
            ref.id
        } catch (e: FirebaseException) { null }
    }

    suspend fun setCurrentProfile(update: Profile): Boolean {
        val uid = getCurrentUserId() ?: return false

        return try {
            collection
                .document(uid)
                .set(update)
                .await()
            true
        } catch (e: FirebaseException) { false }
    }

    fun signOut() = Firebase.auth.signOut()

}

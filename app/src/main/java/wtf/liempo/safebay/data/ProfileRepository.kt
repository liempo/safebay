package wtf.liempo.safebay.data

import com.google.firebase.FirebaseException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import wtf.liempo.safebay.common.models.Profile

class ProfileRepository {

    private val profiles = Firebase.firestore
        .collection("profile")

    fun getCurrentUserId(): String? =
        Firebase.auth.currentUser?.uid

    suspend fun getCurrentProfile(): Profile? =
        getProfile(getCurrentUserId() ?:  "")

    suspend fun getProfile(uid: String): Profile? {
        if (uid.isEmpty()) return null

        return try {
            profiles
                .document(uid)
                .get()
                .await()
                .toObject<Profile>()
        } catch (e: FirebaseException) { null }
    }

    suspend fun setCurrentProfile(update: Profile): Boolean {
        val uid = getCurrentUserId() ?: return false

        return try {
            profiles
                .document(uid)
                .set(update)
                .await()
            true
        } catch (e: FirebaseException) { false }
    }

}

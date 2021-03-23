package wtf.liempo.safebay.auth.data

import com.google.firebase.FirebaseException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import wtf.liempo.safebay.common.models.Profile

class AuthRepository {

    fun getCurrentUserId(): String? =
        Firebase.auth.currentUser?.uid

    suspend fun getCurrentProfile(): Profile? {
        val uid = getCurrentUserId() ?: return null

        return try {
            Firebase.firestore
                .collection("profile")
                .document(uid)
                .get()
                .await()
                .toObject<Profile>()
        } catch (e: FirebaseException) { null }
    }

    suspend fun setCurrentProfile(update: Profile): Boolean {
        val uid = getCurrentUserId() ?: return false

        return try {
            Firebase.firestore
                .collection("profile")
                .document(uid)
                .set(update)
                .await()
            true
        } catch (e: FirebaseException) { false }
    }

}

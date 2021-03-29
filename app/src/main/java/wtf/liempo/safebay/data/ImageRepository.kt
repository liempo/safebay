package wtf.liempo.safebay.data

import android.net.Uri
import com.google.firebase.FirebaseException
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class ImageRepository {

    private val profileImageRef = Firebase.storage
        .reference.child("profile_photo")

    suspend fun uploadProfileImage(
        uid: String,
        photoUri: String
    ): String? =
         try {
           val uri = Uri.parse(photoUri)
            val ref = profileImageRef.child(uid)
                .also {
                    it.putFile(uri)
                        .await()
                }
            ref.downloadUrl
                .await()
                .toString()
        } catch (e: FirebaseException) {
            null
        }

}
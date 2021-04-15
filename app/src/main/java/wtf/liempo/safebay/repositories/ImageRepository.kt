package wtf.liempo.safebay.repositories

import android.net.Uri
import com.google.firebase.FirebaseException
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class ImageRepository {

    private val reference = Firebase.storage
        .reference.child("profile_images")

    suspend fun uploadProfileImage(
        uid: String,
        photoUri: String
    ): String? =
         try {
           val uri = Uri.parse(photoUri)
            val ref = reference.child(uid)
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

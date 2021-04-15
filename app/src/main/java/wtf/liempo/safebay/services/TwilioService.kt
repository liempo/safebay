package wtf.liempo.safebay.services

import com.google.gson.Gson
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import timber.log.Timber
import wtf.liempo.safebay.models.SMSRequest

class TwilioService {

    fun post(smsRequest: SMSRequest): String? {
        val client = OkHttpClient()
        val json = Gson().toJson(smsRequest)

        Timber.d("Request Body: $json")
        val body = RequestBody.create(JSON, json)
        val request = Request.Builder()
            .url(URL_GROUP_SMS)
            .post(body)
            .build()

        return try {
            client.newCall(request)
                .execute()
                .body()
                .string()
        } catch (e: Exception) {
            Timber.e(e, "Failed calling executing POST.")
            null
        }
    }

    companion object {
        private const val URL = "https://safebay.herokuapp.com/"
        private const val URL_GROUP_SMS = URL + "sendGroupSMS"
        private val JSON = MediaType.parse(
            "application/json; charset=utf-8")
    }
}

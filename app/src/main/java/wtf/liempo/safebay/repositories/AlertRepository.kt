package wtf.liempo.safebay.repositories

import wtf.liempo.safebay.models.SMSRequest
import wtf.liempo.safebay.services.TwilioService

class AlertRepository {

    private val service = TwilioService()

    suspend fun sendAlert(recipients: List<String>, msg: String) {
        val request = SMSRequest(
            recipients.toList(), msg)
        service.post(request)
    }
}

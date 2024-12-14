package cityu.ai510.MedTranslate

import cityu.ai510.MedTranslate.server.RetrofitClient
import cityu.ai510.MedTranslate.server.SynthesizeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SpeechManager {

    suspend fun generateAudio(text: String, toLang: String): String {
        return try {
            val synthesizeRequest = SynthesizeRequest(text, toLang)
            println(synthesizeRequest)

            withContext(Dispatchers.IO) {
                val synthesizeResponse =
                    RetrofitClient.apiService.synthesizeText(synthesizeRequest)
                println(synthesizeResponse)
                synthesizeResponse.speechUrl
            }
        } catch (e: Exception) {
            println("Error fetching user: ${e.localizedMessage}")
            ""
        }
    }
}
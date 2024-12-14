package cityu.ai510.MedTranslate

import cityu.ai510.MedTranslate.server.ProcessRequest
import cityu.ai510.MedTranslate.server.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProcessingManager {

    private val modeFullText = "full_text"

    suspend fun processFileId(id: String, fromLanguage: String, toLanguage: String): String {
        return try {
            val processRequest = ProcessRequest(id, modeFullText, fromLanguage, toLanguage)
            println(processRequest)

            withContext(Dispatchers.IO) {
                val processResponse =
                    RetrofitClient.apiService.processFile(processRequest)
                println(processResponse)
                processResponse.translationResult.translatedText
            }
        } catch (e: Exception) {
            println("Error fetching user: ${e.localizedMessage}")
            ""
        }
    }
}
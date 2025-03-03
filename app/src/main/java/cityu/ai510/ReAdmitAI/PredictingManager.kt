package cityu.ai510.ReAdmitAI

import cityu.ai510.ReAdmitAI.server.PredictRequest
import cityu.ai510.ReAdmitAI.server.PredictResponse
import cityu.ai510.ReAdmitAI.server.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PredictingManager {

    suspend fun predict(predictRequest: PredictRequest): PredictResponse {
        return try {
            println(predictRequest)
            withContext(Dispatchers.IO) {
                val predictResponse =
                    RetrofitClient.apiService.predictReadmission(predictRequest)
                println(predictResponse)
                predictResponse
            }
        } catch (e: Exception) {
            println("Error predicting : ${e.localizedMessage}")
            PredictResponse("", "")
        }
    }
}
package cityu.ai510.ReAdmitAI.server

import retrofit2.http.*

data class PredictRequest(
    val number_outpatient: Int,
    val change: String,
    val gender: String,
    val age: String,
    val diabetesMed: String,
    val time_in_hospital: Int,
    val num_medications: Int,
    val number_diagnoses: Int,
)

data class PredictResponse(
    val prediction: String,
    val risk_score: String
)

interface ApiService {
    @POST("predict")
    suspend fun predictReadmission(@Body predictRequest: PredictRequest): PredictResponse
}
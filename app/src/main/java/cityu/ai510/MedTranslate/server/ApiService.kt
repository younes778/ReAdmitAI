package cityu.ai510.MedTranslate.server

import okhttp3.RequestBody
import retrofit2.http.*

data class UpdateResponse(
    val message: String,
    val fileId: String,
    val fileUrl: String
)

data class ProcessRequest(
    val fileId: String,
    val mode: String,
    val sourceLanguage: String,
    val targetLanguage: String
)

data class TranslationResult(
    val originalText: String,
    val translatedText: String,
    val sourceLanguage: String
)

data class ProcessResponse(
    val translationResult: TranslationResult
)

data class SynthesizeRequest(
    val text: String,
    val targetLanguage: String
)

data class SynthesizeResponse(
    val speechUrl: String
)

interface ApiService {
    @PUT("upload/{file_name}")
    suspend fun uploadFile(
        @Path("file_name") fineName: String,
        @Body fileBody: RequestBody
    ): UpdateResponse

    @POST("process")
    suspend fun processFile(@Body processRequest: ProcessRequest): ProcessResponse

    @POST("synthesize")
    suspend fun synthesizeText(@Body synthesizeRequest: SynthesizeRequest): SynthesizeResponse
}
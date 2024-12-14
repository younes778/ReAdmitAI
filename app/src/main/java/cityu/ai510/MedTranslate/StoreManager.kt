package cityu.ai510.MedTranslate

import android.content.ContentResolver
import android.net.Uri
import cityu.ai510.MedTranslate.server.RetrofitClient
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.source
import java.util.UUID

class StoreManager(private val contentResolver: ContentResolver) {

    private fun createFileRequestBody(uri: Uri): RequestBody {
        val inputStream = contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open InputStream for URI: $uri")
        return object : RequestBody() {
            override fun contentType() = "application/octet-stream".toMediaTypeOrNull()

            override fun writeTo(sink: okio.BufferedSink) {
                inputStream.source().use { source ->
                    sink.writeAll(source)
                }
            }
        }
    }

    suspend fun uploadFile(file: MPFile<Any>): String {
        return try {
            withContext(Dispatchers.IO) {
                val fileName = UUID.randomUUID().toString()
                val updateResponse =
                    RetrofitClient.apiService.uploadFile(
                        fileName,
                        createFileRequestBody(file.platformFile as Uri)
                    )
                println(updateResponse)
                updateResponse.fileId
            }
        } catch (e: Exception) {
            println("Error fetching user: ${e.localizedMessage}")
            ""
        }
    }
}
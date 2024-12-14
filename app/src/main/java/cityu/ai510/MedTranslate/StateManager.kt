package cityu.ai510.MedTranslate

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import com.rejowan.ccpc.Country
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

enum class State {
    NO_FILE, UPLOADING_IMAGE, PROCESSING, GENERATING_AUDIO, DONE, ERROR
}

val SUPPORTED_COUNTRIES: List<Country> = listOf(
    Country.UnitedStates,
    Country.Spain,
    Country.France,
    Country.Portugal,
    Country.Italy,
    Country.Germany,
    Country.RussianFederation,
    Country.SaudiArabia
)

val SUPPORTED_IMAGE_EXTENSION: List<String> = listOf(
    "png", "jpg", "jpeg", "webp", "bmp"
)

class StateManager(state: State, context: Context) {

    private val emptyData = "Nothing to show yet!"

    constructor(context: Context) : this(State.NO_FILE, context)

    var state: MutableState<State> = mutableStateOf(state)

    var fromCountry: MutableState<Country> = mutableStateOf(Country.UnitedStates)

    var toCountry: MutableState<Country> = mutableStateOf(Country.Spain)

    val imagePath: MutableState<String> = mutableStateOf("")

    val audioUrl: MutableState<String> = mutableStateOf("")

    val data: MutableState<String> = mutableStateOf(emptyData)

    private val storeManager = StoreManager(context.contentResolver)
    private val processingManager = ProcessingManager()
    private val speechManager = SpeechManager()

    fun getStateText(mgrState: State): String {
        when (mgrState) {
            State.NO_FILE -> {
                return "Upload an image to get started!"
            }
            State.UPLOADING_IMAGE -> {
                return "Uploading image ..."
            }
            State.PROCESSING -> {
                return "Image uploaded, processing ..."
            }
            State.GENERATING_AUDIO -> {
                return "Translation ready, generating audio ..."
            }
            State.DONE -> {
                return "Audio generated, Play to hear translation!"
            }
            State.ERROR -> {
                return "Sorry, operation halted due to an error!"
            }
            else -> return ""
        }
    }

    fun getResultTitle(mgrState: State): String {
        return when (mgrState) {
            State.GENERATING_AUDIO -> {
                "Translation"
            }

            State.DONE -> {
                "Translation"
            }

            else -> "No Updates Yet"
        }
    }

    fun showProgressIndicator(mgrState: State): Boolean {
        return mgrState != State.NO_FILE && mgrState != State.ERROR && mgrState != State.DONE
    }

    fun enabledUploadButton(mgrState: State): Boolean {
        return !showProgressIndicator(mgrState)
    }

    fun showError(mgrState: State): Boolean {
        return mgrState == State.ERROR
    }

    fun showAudioButton(mgrState: State): Boolean {
        return mgrState == State.DONE
    }

    fun showNoFile(mgrState: State): Boolean {
        return mgrState == State.NO_FILE
    }

    private fun countryToLang(country: Country): String {
        return when (country) {
            Country.UnitedStates -> {
                "en"
            }

            Country.SaudiArabia -> {
                "ar"
            }

            else -> country.countryIso.lowercase()
        }
    }

    fun reset() {
        state.value = State.NO_FILE
        imagePath.value = ""
        data.value = emptyData
        audioUrl.value = ""
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun uploadAndTranslate(platformFile: MPFile<Any>) {
        state.value = State.UPLOADING_IMAGE
        imagePath.value = platformFile.path
        data.value = emptyData

        GlobalScope.launch(Dispatchers.Main) {
            val id: String = storeManager.uploadFile(platformFile)
            if (id.isEmpty()) {
                state.value = State.ERROR
                return@launch
            }
            state.value = State.PROCESSING

            data.value = processingManager.processFileId(
                id,
                countryToLang(fromCountry.value),
                countryToLang(toCountry.value)
            )
            if (data.value.isEmpty()) {
                state.value = State.ERROR
                return@launch
            }

            state.value = State.GENERATING_AUDIO

            audioUrl.value = speechManager.generateAudio(data.value, countryToLang(toCountry.value))
            if (audioUrl.value.isEmpty()) {
                state.value = State.ERROR
                return@launch
            }
            state.value = State.DONE
        }

    }
}

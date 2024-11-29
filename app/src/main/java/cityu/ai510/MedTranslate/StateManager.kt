package cityu.ai510.MedTranslate;

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import com.rejowan.ccpc.Country
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class State {
    NO_FILE, UPLOADING_IMAGE, EXTRACTING_DATA, TRANSLATING, GENERATING_AUDIO, DONE, ERROR
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

class StateManager(state: State) {

    constructor() : this(State.NO_FILE)

    var state: MutableState<State> = mutableStateOf(state)

    var fromCountry: MutableState<Country> = mutableStateOf(Country.UnitedStates)

    var toCountry: MutableState<Country> = mutableStateOf(Country.France)

    val imagePath: MutableState<String> = mutableStateOf("")

    val audioUrl: MutableState<String> = mutableStateOf("")

    val data: MutableState<List<String>> = mutableStateOf(listOf("Nothing to show yet!"))

    private val storeManager = StoreManager()
    private val extractionManager = ExtractionManager()
    private val translationManager = TranslationManager()
    private val speechManager = SpeechManager()

    fun getStateText(mgrState: State): String {
        if (mgrState == State.NO_FILE) {
            return "Upload an image to get started!"
        } else if (mgrState == State.UPLOADING_IMAGE) {
            return "Uploading image ..."
        } else if (mgrState == State.EXTRACTING_DATA) {
            return "Image uploaded, extracting data ..."
        } else if (mgrState == State.TRANSLATING) {
            return "Data extracted, translating ..."
        } else if (mgrState == State.GENERATING_AUDIO) {
            return "Translation ready, generating audio ..."
        } else if (mgrState == State.DONE) {
            return "Audio generated, Play to hear translation!"
        } else if (mgrState == State.ERROR) {
            return "Sorry, operation halted due to an error!"
        } else return ""
    }

    fun getResultTitle(mgrState: State): String {
        if (mgrState == State.TRANSLATING) {
            return "Extracted Medical Info"
        } else if (mgrState == State.GENERATING_AUDIO) {
            return "Translation"
        } else if (mgrState == State.DONE) {
            return "Translation"
        } else return "No Updates Yet"
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

    fun countryToLang(country: Country): String {
        if (country == Country.UnitedStates) {
            return "en"
        } else if (country == Country.SaudiArabia) {
            return "ar"
        } else return country.countryIso
    }

    fun reset() {
        state.value = State.NO_FILE
        imagePath.value = ""
        data.value = listOf("Nothing to show yet!")
        audioUrl.value = ""
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun uploadAndTranslate(platformFile: MPFile<Any>) {
        state.value = State.UPLOADING_IMAGE
        imagePath.value = platformFile.path
        data.value = listOf("Nothing to show yet!")

        GlobalScope.launch(Dispatchers.Main) {
            delay(5000)
            val id: String = storeManager.uploadFile(platformFile)
            if (id.isEmpty()) {
                state.value = State.ERROR
                return@launch
            }

            state.value = State.EXTRACTING_DATA
            delay(5000)
            data.value = extractionManager.extractInfo(id)
            if (data.value.isEmpty()) {
                state.value = State.ERROR
                return@launch
            }

            state.value = State.TRANSLATING
            delay(5000)
            data.value = translationManager.translate(
                countryToLang(fromCountry.value),
                countryToLang(toCountry.value), data.value
            )
            if (data.value.isEmpty()) {
                state.value = State.ERROR
                return@launch
            }

            state.value = State.GENERATING_AUDIO
            delay(5000)
            audioUrl.value = speechManager.generateAudio(countryToLang(toCountry.value), data.value)
            state.value = State.DONE
        }

    }
}

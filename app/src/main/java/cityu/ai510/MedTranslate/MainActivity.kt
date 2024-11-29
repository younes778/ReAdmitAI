package cityu.ai510.MedTranslate

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.KeyboardArrowRight
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.sharp.Info
import androidx.compose.material.icons.sharp.KeyboardArrowRight
import androidx.compose.material.icons.sharp.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import cityu.ai510.MedTranslate.ui.theme.MyApplicationTheme
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.rejowan.ccpc.CountryCodePicker
import com.rejowan.ccpc.PickerCustomization
import com.rejowan.ccpc.ViewCustomization

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val context = LocalContext.current
                val state = remember { mutableStateOf(StateManager()) }
                val showFilePicker = remember { mutableStateOf(false) }
                Surface(modifier = Modifier.fillMaxSize()) {
                    ShowImagePicker(state, showFilePicker)
                    MainUI(state, showFilePicker, context)
                }
            }
        }
    }
}

@Composable
fun MainUI(
    state: MutableState<StateManager>,
    showFilePicker: MutableState<Boolean>,
    context: Context
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
        ) {
            ImageUI(state, context)
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
        ) {

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(40.dp))
                .background(Color.White)
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UpdateButton(state, showFilePicker = showFilePicker)
            TranslationUI(state, context)
        }
    }
}

@Composable
fun ImageUI(state: MutableState<StateManager>, context: Context) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val imagePath by state.value.imagePath
        if (imagePath.isNotEmpty()) {
            val uri = Uri.parse(imagePath)
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = ""
                )
                return
            }
        }
        Image(
            painter = painterResource(R.drawable.landscape_bg),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
fun TranslationUI(state: MutableState<StateManager>, context: Context) {
    Column(
        modifier = Modifier
            .padding(0.dp, 20.dp, 0.dp, 0.dp)
            .fillMaxSize(), horizontalAlignment = Alignment.Start
    ) {
        LanguageUI(state)
        Spacer(Modifier.size(20.dp))
        StatusBar(state, context)
        Spacer(Modifier.size(40.dp))
        ResultUI(state)
    }
}

@Composable
fun LanguageUI(state: MutableState<StateManager>) {
    Row {
        Text(
            modifier = Modifier
                .wrapContentHeight()
                .align(Alignment.CenterVertically),
            text = "From",
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            color = colorResource(R.color.secondary_text)
        )
        val fromCountry by state.value.fromCountry
        val toCountry by state.value.toCountry
        CountryCodePicker(
            selectedCountry = fromCountry,
            onCountrySelected = { state.value.fromCountry.value = it },
            countryList = SUPPORTED_COUNTRIES,
            viewCustomization = ViewCustomization(
                showFlag = true,
                showCountryIso = true,
                showCountryName = false,
                showCountryCode = false,
                clipToFull = false
            ),
            pickerCustomization = PickerCustomization(
                showFlag = true,
                showCountryIso = true,
                showCountryCode = false,
                headerTitle = "Select Language",
            ),
            showSheet = true,
        )
        Spacer(Modifier.size(0.dp))
        Text(
            modifier = Modifier
                .wrapContentHeight()
                .align(Alignment.CenterVertically),
            text = "To",
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            color = colorResource(R.color.secondary_text)
        )
        CountryCodePicker(
            selectedCountry = toCountry,
            countryList = SUPPORTED_COUNTRIES,
            onCountrySelected = { state.value.toCountry.value = it },
            viewCustomization = ViewCustomization(
                showFlag = true,
                showCountryIso = true,
                showCountryName = false,
                showCountryCode = false,
                clipToFull = false
            ),
            pickerCustomization = PickerCustomization(
                showFlag = true,
                showCountryIso = true,
                showCountryCode = false,
                headerTitle = "Select Language"
            ),
            showSheet = true,
        )
    }
}

@Composable
fun StatusBar(state: MutableState<StateManager>, context: Context) {
    Column(modifier = Modifier.wrapContentHeight(), horizontalAlignment = Alignment.Start) {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            val mgrState by state.value.state
            val audioUrl by state.value.audioUrl
            if (state.value.showAudioButton(mgrState)) {
                Icon(
                    Icons.Rounded.PlayArrow,
                    tint = colorResource(R.color.primary),
                    contentDescription = "",
                    modifier = Modifier
                        .wrapContentSize()
                        .width(60.dp)
                        .height(60.dp)
                        .clickable {
                            if (audioUrl.isNotEmpty()) {
                                val player = ExoPlayer
                                    .Builder(context)
                                    .build()
                                val mediaItem = MediaItem.fromUri(audioUrl)
                                player.setMediaItem(mediaItem)
                                player.prepare()
                                player.play()
                            }
                        }
                )
            }
            if (state.value.showProgressIndicator(mgrState)) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .wrapContentSize()
                        .width(60.dp)
                        .height(60.dp),
                    color = colorResource(R.color.secondary_text),
                    trackColor = colorResource(R.color.primary),
                )
            }
            if (state.value.showError(mgrState)) {
                Icon(
                    Icons.Sharp.Warning,
                    tint = colorResource(R.color.primary),
                    contentDescription = "",
                    modifier = Modifier
                        .wrapContentSize()
                        .width(60.dp)
                        .height(60.dp)
                )
            }
            if (state.value.showNoFile(mgrState)) {
                Icon(
                    Icons.Sharp.Info,
                    tint = colorResource(R.color.primary),
                    contentDescription = "",
                    modifier = Modifier
                        .wrapContentSize()
                        .width(60.dp)
                        .height(60.dp)
                )
            }
            Spacer(Modifier.size(20.dp))
            Row(
                modifier = Modifier.height(70.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentHeight()
                        .align(Alignment.CenterVertically),
                    text = state.value.getStateText(mgrState),
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    color = colorResource(R.color.primary_text)
                )
            }
        }

    }
}


@Composable
fun ResultUI(state: MutableState<StateManager>) {
    Column(modifier = Modifier.wrapContentHeight(), horizontalAlignment = Alignment.Start) {
        val mgrState by state.value.state
        val data by state.value.data
        Text(
            modifier = Modifier
                .wrapContentHeight(),
            text = state.value.getResultTitle(mgrState),
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.primary_text)
        )
        Spacer(Modifier.size(10.dp))
        Column(
            modifier = Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.Start
        ) {
            data.map { text ->
                Row {
                    Icon(
                        Icons.AutoMirrored.Sharp.KeyboardArrowRight,
                        tint = colorResource(R.color.secondary_text),
                        contentDescription = "",
                        modifier = Modifier
                            .wrapContentSize()
                            .width(25.dp)
                            .height(25.dp)
                    )
                    Text(
                        text = text,
                        textAlign = TextAlign.Start,
                        fontSize = 18.sp,
                        color = colorResource(R.color.secondary_text)
                    )
                }
                Spacer(Modifier.size(10.dp))
            }
        }
    }
}


@Composable
fun UpdateButton(state: MutableState<StateManager>, showFilePicker: MutableState<Boolean>) {

    val buttonHeight = 70.dp
    val mgrState by state.value.state
    ExtendedFloatingActionButton(
        onClick = {
            if (state.value.enabledUploadButton(mgrState)) {
                showFilePicker.value = true
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(buttonHeight)
            .clip(RoundedCornerShape(60.dp)),
        containerColor =
        if (state.value.enabledUploadButton(mgrState)) {
            colorResource(id = R.color.primary)
        } else Color.Gray,
    ) {
        Text(
            "Upload & Translate",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun ShowImagePicker(
    state: MutableState<StateManager>,
    showFilePicker: MutableState<Boolean>
) {
    val fileType = SUPPORTED_IMAGE_EXTENSION

    FilePicker(show = showFilePicker.value, fileExtensions = fileType) { platformFile ->
        showFilePicker.value = false
        if (platformFile == null) {
            state.value.reset()
        } else {
            state.value.uploadAndTranslate(platformFile)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        val context = LocalContext.current
        val state = remember { mutableStateOf(StateManager(State.TRANSLATING)) }
        val showFilePicker = remember { mutableStateOf(false) }
        ShowImagePicker(state, showFilePicker)
        MainUI(state, showFilePicker, context)
    }
}
package cityu.ai510.ReAdmitAI

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cityu.ai510.ReAdmitAI.server.PredictRequest
import cityu.ai510.ReAdmitAI.server.PredictResponse
import cityu.ai510.ReAdmitAI.survey.HospitalInformation
import cityu.ai510.ReAdmitAI.survey.MedicationInformation
import cityu.ai510.ReAdmitAI.survey.Navigation
import cityu.ai510.ReAdmitAI.survey.PersonalInformation
import cityu.ai510.ReAdmitAI.survey.TabLayout
import com.patrik.fancycomposedialogs.dialogs.ErrorFancyDialog
import com.patrik.fancycomposedialogs.dialogs.SuccessFancyDialog
import com.patrik.fancycomposedialogs.enums.DialogActionType
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SurveyActivity : ComponentActivity() {
    private val viewmodel: SurveyViewmodel by viewModels()
    private val predictingManager= PredictingManager()
    private val prediction: MutableState<String> = mutableStateOf("")
    private val risk_score: MutableState<String> = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val screen by remember { viewmodel.screen }
                val formState = remember { viewmodel.formState }
                val validate = intent.getBooleanExtra("validate", true)

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    TabLayout(screen = screen)

                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        when (screen) {
                            0 -> PersonalInformation(formState = formState)
                            1 -> HospitalInformation(formState = formState)
                            2 -> MedicationInformation(formState = formState)
                        }

                        Spacer(modifier = Modifier.size(32.dp))

                        Navigation(screen = screen) {
                            if (validate) {
                                viewmodel.validateScreen(screen)
                            } else {
                                if (screen == 2) viewmodel.validateSurvey()
                                else viewmodel.navigate(screen = screen + 1)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.size(100.dp))
                }

                var showSuccessDialog by remember { mutableStateOf(false) }
                var showFailureDialog by remember { mutableStateOf(false) }

                if (showSuccessDialog) {
                    SuccessFancyDialog(title = "Prediction results",
                        message = risk_score.value,
                        dialogActionType = DialogActionType.INFORMATIVE,
                        dismissTouchOutside = {
                            showSuccessDialog = false
                            viewmodel.reset()
                        },
                        neutralButtonClick = {
                            showSuccessDialog = false
                            viewmodel.reset()
                        })
                }

                if (showFailureDialog) {
                    ErrorFancyDialog(title = "Prediction results",
                        message = risk_score.value,
                        dialogActionType = DialogActionType.INFORMATIVE,
                        dismissTouchOutside = {
                            showFailureDialog = false
                        },
                        neutralButtonClick = {
                            showFailureDialog = false
                        })
                }

                val processDone by remember { viewmodel.finish }
                if (processDone) {
                    val request = viewmodel.transformData()
                    predictResults(request, predictingManager, prediction, risk_score)
                    if (prediction.value.isNotEmpty()) {
                        if (prediction.value.equals("Yes")) {
                            showFailureDialog = true
                        } else {
                            showSuccessDialog = true
                        }
                    }
                }
            }
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun predictResults(
    request: PredictRequest,
    predictingManager: PredictingManager,
    prediction: MutableState<String>,
    risk_score: MutableState<String>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val response:PredictResponse = predictingManager.predict(request)
        if (response.prediction.isEmpty()) {
            return@launch
        }
        prediction.value = response.prediction
        risk_score.value = response.risk_score
    }
}
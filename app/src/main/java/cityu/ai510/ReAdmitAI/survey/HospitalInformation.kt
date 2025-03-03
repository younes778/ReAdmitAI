package cityu.ai510.ReAdmitAI.survey


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dsc.form_builder.BaseState
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState

@Composable
fun HospitalInformation(formState: FormState<BaseState<*>>) {

    val timeInHospitalState: TextFieldState = formState.getState("time_in_hospital")
    val numberOutpatientState: TextFieldState = formState.getState("number_outpatient")
    val numberDiagnosesState: TextFieldState = formState.getState("number_diagnoses")


    Column(horizontalAlignment = CenterHorizontally, verticalArrangement = Center) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Hospital Information",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(30.dp))

        TextInput(label = "Number of times in hospital", state = timeInHospitalState)

        Spacer(modifier = Modifier.height(10.dp))

        TextInput(label = "Number of outpatient visits", state = numberOutpatientState)

        Spacer(modifier = Modifier.height(10.dp))

        TextInput(label = "Number of diagnoses", state = numberDiagnosesState)
    }
}

@Preview
@Composable
fun HospitalInformationPreview() {
    val formState: FormState<BaseState<*>> = FormState(
        listOf(
            TextFieldState("time_in_hospital"),
            TextFieldState("number_outpatient"),
            TextFieldState("number_diagnoses")
        )
    )
    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            HospitalInformation(formState)
        }
    }
}
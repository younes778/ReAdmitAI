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
import com.dsc.form_builder.ChoiceState
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState

@Composable
fun MedicationInformation(formState: FormState<BaseState<*>>) {

    val changeState: ChoiceState = formState.getState("change")
    val diabetesMedState: ChoiceState = formState.getState("diabetesMed")
    val num_medicationsState: TextFieldState = formState.getState("num_medications")

    val changeOptions = listOf("Yes", "No")

    Column(horizontalAlignment = CenterHorizontally, verticalArrangement = Center) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Medication Information",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(30.dp))

        ChoicesRow(labelText = "Medication change", items = changeOptions, changeState)

        Spacer(modifier = Modifier.height(10.dp))

        ChoicesRow(labelText = "Diabetes meds taken", items = changeOptions, diabetesMedState)

        Spacer(modifier = Modifier.height(10.dp))

        TextInput(label = "Number of medications", state = num_medicationsState)
    }
}

@Preview
@Composable
fun MedicationInformationPreview() {
    val formState: FormState<BaseState<*>> = FormState(
        listOf(
            ChoiceState("change", validators = listOf()),
            TextFieldState("diabetesMed"),
            TextFieldState("num_medications")
        )
    )
    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            MedicationInformation(formState)
        }
    }
}
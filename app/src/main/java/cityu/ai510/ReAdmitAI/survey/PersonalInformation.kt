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
fun PersonalInformation(formState: FormState<BaseState<*>>) {

    val genderState: ChoiceState = formState.getState("gender")

    val ageState: TextFieldState = formState.getState("age")

    val genderOptions = listOf(Gender.Male.toString(), Gender.Female.toString(), Gender.Other.toString())

    Column(horizontalAlignment = CenterHorizontally, verticalArrangement = Center) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Personal Information",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(30.dp))

        ChoicesRow(labelText = "Gender", items = genderOptions, genderState)

        Spacer(modifier = Modifier.height(10.dp))

        TextInput(label = "Age", state = ageState)
    }
}

@Preview
@Composable
fun PersonalInformationPreview() {
    val formState: FormState<BaseState<*>> = FormState(
        listOf(
            ChoiceState("gender", validators = listOf()),
            TextFieldState("age")
        )
    )
    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PersonalInformation(formState)
        }
    }
}
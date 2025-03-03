package cityu.ai510.ReAdmitAI.survey

import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dsc.form_builder.ChoiceState
import com.dsc.form_builder.TextFieldState


@Composable
fun ChoicesRow(labelText: String, items: List<String>, state: ChoiceState) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = labelText, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.weight(1f))

            items.forEach { item ->
                Column(
                    modifier = Modifier.width(85.dp).selectableGroup(),
                    verticalArrangement = Center,
                    horizontalAlignment = CenterHorizontally
                ) {
                    Text(
                        text = item, style = MaterialTheme.typography.labelSmall
                    )
                    RadioButton(
                        selected = state.value == item,
                        onClick = { state.change(item) }
                    )
                }
            }
        }

        if (state.hasError) {
            Text(
                text = state.errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun TextInput(label: String, state: TextFieldState) {
    Column {
        OutlinedTextField(
            value = state.value,
            isError = state.hasError,
            label = { Text(text = label) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            onValueChange = {
                if (it.matches(Regex("\\d*"))) {  // Allows only digits (0-9)
                    state.change(it)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),  // Numeric Keyboard

            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Gray,
                errorBorderColor = MaterialTheme.colorScheme.error,
            )
        )

        if (state.hasError) {
            Text(
                text = state.errorMessage,
                modifier = Modifier.padding(start = 12.dp, top = 4.dp),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
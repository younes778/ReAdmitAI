package cityu.ai510.ReAdmitAI.survey

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cityu.ai510.MedTranslate.R


@Composable
@Preview(showBackground = true)
fun NavigationPreview() {
    var screen by remember { mutableStateOf(0) }

    MaterialTheme {
        Navigation(screen = screen) {
            screen = if (screen == 2) 0 else screen + 1
        }
    }
}

@Composable
fun Navigation(screen: Int, navigate: () -> Unit) {
    val shape = MaterialTheme.shapes.large
    val white = Color.White
    val color = Color.Black
    val textStyle = MaterialTheme.typography.displayMedium

    val buttonColors = ButtonDefaults.buttonColors(containerColor = color)

    val text = if (screen != 2) "Next" else "Submit"

    Button(
        shape = shape,
        onClick = navigate,
        colors = buttonColors,
        modifier = Modifier.width(200.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 15.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = text, style = textStyle.copy(fontSize = 12.sp, color = white))

            Spacer(modifier = Modifier.width(15.dp))

            Icon(
                tint = white,
                contentDescription = "arrow",
                painter = painterResource(id = R.drawable.ic_arrow),
            )
        }
    }
}
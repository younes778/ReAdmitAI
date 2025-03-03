package cityu.ai510.ReAdmitAI


import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import cityu.ai510.ReAdmitAI.server.PredictRequest
import cityu.ai510.ReAdmitAI.survey.Gender
import cityu.ai510.ReAdmitAI.survey.SurveyModel
import com.dsc.form_builder.*

class SurveyViewmodel : ViewModel() {

    private val _screen: MutableState<Int> = mutableStateOf(0)
    val screen: State<Int> = _screen

    private val _finish: MutableState<Boolean> = mutableStateOf(false)
    val finish: State<Boolean> = _finish

    val formState: FormState<BaseState<*>> = FormState(
        fields = listOf(
            ChoiceState("gender", validators = listOf(Validators.Required(
                message = "Select your gender"
            ))),
            TextFieldState(
                name = "age",
                validators = listOf(
                    Validators.MinValue(0, "Minimum age is 0"),
                    Validators.Required(message = "Age is required"),
                )
            ),
            TextFieldState(
                name = "time_in_hospital",
                validators = listOf(
                    Validators.Required(message = "Number of times in hospital is required"),
                )
            ),
            TextFieldState(
                name = "number_outpatient",
                validators = listOf(
                    Validators.Required(message = "Number of outpatient visits is required"),
                )
            ),
            TextFieldState(
                name = "number_diagnoses",
                validators = listOf(
                    Validators.Required(message = "Number of diagnoses is required"),
                )
            ),
            ChoiceState(
                name = "change",
                validators = listOf(
                    Validators.Required(
                        message = "Medication change must be indicated"
                    )
                )
            ),
            ChoiceState(
                name = "diabetesMed",
                validators = listOf(
                    Validators.Required(message = "Diabetes meds taken or not is required"),
                )
            ),
            TextFieldState(
                name = "num_medications",
                validators = listOf(
                    Validators.Required(message = "Number of medication is required"),
                )
            )
        )
    )

    fun navigate(screen: Int) {
        _screen.value = screen
    }

    fun validateSurvey() {
        val pages: List<List<Int>> = (0..5).chunked(3)
        if (!formState.validate()){
            val position = formState.fields.indexOfFirst { it.hasError }
            _screen.value = pages.indexOfFirst { it.contains(position) }
        } else {
            logData()
            _finish.value = true
        }
    }

    fun validateScreen(screen: Int) {
        val fields: List<BaseState<*>>
        if (screen == 0) {
            fields = formState.fields.chunked(2)[0]
        } else if (screen == 1) {
            fields = formState.fields.chunked(5)[0]
        } else {
            fields = formState.fields
        }
        if (fields.map { it.validate() }.all { it }){ // map is used so we can execute validate() on all fields in that screen
            if (screen == 2){
                logData()
                _finish.value = true
            }
            _screen.value += 1
        }
    }

    fun reset() {
        _screen.value = 0
        _finish.value = false
    }

    fun transformData():PredictRequest {
        var gender:String = formState.fields[0].value.toString()
        if (gender == Gender.Other.toString()) {
            gender = "Unknown/Other"
        }
        val ageValue:Int = formState.fields[1].value.toString().toInt()
        val age:String
        if (ageValue <=20) {
            age = "Age 0-20"
        } else if (ageValue <= 70) {
            age = "Age 20-70"
        } else {
            age = "Age 70-100"
        }
        val time_in_hospital:Int = formState.fields[2].value.toString().toInt()
        val number_outpatient:Int = formState.fields[3].value.toString().toInt()
        val number_diagnoses:Int = formState.fields[4].value.toString().toInt()
        var change:String = formState.fields[5].value.toString()
        if (change == "Yes") {
            change = "Ch"
        } else {
            change = "No"
        }
        val diabetesMed:String = formState.fields[6].value.toString()
        val num_medications:Int = formState.fields[7].value.toString().toInt()

        return PredictRequest(number_outpatient, change, gender, age, diabetesMed, time_in_hospital,
            num_medications, number_diagnoses)
    }

    private fun logData() {
        val data = formState.getData(SurveyModel::class)
        Log.d("SurveyLog", "form data is $data")
    }
}
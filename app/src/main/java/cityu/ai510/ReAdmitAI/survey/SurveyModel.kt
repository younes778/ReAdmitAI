package cityu.ai510.ReAdmitAI.survey

data class SurveyModel(
    // First page
    val gender: String,
    val age: String,

    // Second Page
    val time_in_hospital: String,
    val number_outpatient: String,
    val number_diagnoses: String,

    // Third Page
    val change: String,
    val diabetesMed: String,
    val num_medications: String,
)

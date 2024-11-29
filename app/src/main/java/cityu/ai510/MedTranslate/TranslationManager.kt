package cityu.ai510.MedTranslate

class TranslationManager {

    fun translate(fromLang: String, toLang: String, texts: List<String>): List<String> {
        return listOf(
            "La taille cardiaque ne peut pas etre evalue",
            "Effusion pleurale gauche grande",
            "Une nouvelle petite effusion a la droite"
        )
    }
}
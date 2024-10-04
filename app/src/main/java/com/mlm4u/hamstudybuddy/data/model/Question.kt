package com.mlm4u.hamstudybuddy.data.model

import android.util.Log
import com.mlm4u.hamstudybuddy.data.database.Questions
import com.squareup.moshi.Json


data class Root(
    val sections: List<Section>,
    val metadata: Metadata
) {
    // Diese Funktion durchsucht rekursiv alle Sektionen nach Fragen und setzt den Titel der letzten Section als titleQuestion.
    fun getAllQuestions(): List<Question> {
        val allQuestions = mutableListOf<Question>()

        // Rekursive Hilfsfunktion, um die Sektionen zu durchlaufen
        fun traverseSections(sections: List<Section>?, currentTitle: String? = null) {
            sections?.forEach { section ->
                val titleToUse = section.title
                val questions =
                    section.questions  // Lokale Variable, um die Fragen zwischenzuspeichern
                questions?.forEach { question ->
                    // Setzt den Titel der Section als titleQuestion in der Frage
                    question.titleQuestion = titleToUse
                    Log.d("Traverse", "question.titleQuestion: ${question.titleQuestion}")
                    allQuestions.add(question)
                }

                // Rekursiver Aufruf für die Untersektionen, wobei der aktuelle Titel übergeben wird
                Log.d("Traverse", "Current Title: $titleToUse")
                Log.d("Traverse", "Current Section: ${section.title}")
                traverseSections(section.sections, titleToUse)
            }
        }

        traverseSections(this.sections)
        Log.d("Traverse", "allQuestions: ${allQuestions.size}")
        return allQuestions
    }
}

data class Section(
    val title: String,
    @Json(name = "sections")
    val sections: List<Section>? = null,
    @Json(name = "questions")
    var questions: List<Question>? = null
)

data class Question(
    val number: String,
    @Json(name = "class")
    val classQuestion: String,
    var titleQuestion: String? = null,
    @Json(name = "question")
    var question: String,
    @Json(name = "answer_a")
    val answerA: String,
    @Json(name = "answer_b")
    val answerB: String,
    @Json(name = "answer_c")
    val answerC: String,
    @Json(name = "answer_d")
    val answerD: String,
    @Json(name = "picture_question")
    val pictureQuestion: String? = null,
    @Json(name = "picture_a")
    val pictureA: String? = null,
    @Json(name = "picture_b")
    val pictureB: String? = null,
    @Json(name = "picture_c")
    val pictureC: String? = null,
    @Json(name = "picture_d")
    val pictureD: String? = null,
    var userList: Boolean = false
) {
    fun toUserQuestion(): Questions {
        return Questions(
            number = this.number,
            classQuestion = this.classQuestion,
            titleQuestion = this.titleQuestion ?: "",
            question = this.question,
            answerA = this.answerA,
            answerB = this.answerB,
            answerC = this.answerC,
            answerD = this.answerD,
            pictureQuestion = this.pictureQuestion,
            pictureA = this.pictureA,
            pictureB = this.pictureB,
            pictureC = this.pictureC,
            pictureD = this.pictureD
        )
    }
}

data class Metadata(
    val edition: String,
    @Json(name = "issued_on")
    val issuedOn: String,
    @Json(name = "valid_from")
    val validFrom: String,
    val license: String
)

data class VersionResponse(
    val version: Double
)
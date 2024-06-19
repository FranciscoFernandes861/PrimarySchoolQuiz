package com.example.primaryschoolquiz.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Embedded

@Entity(tableName = "quiz_table")
data class Quiz(
    @PrimaryKey val id: String = "",
    val name: String,
    val creatorId: String,
    val qrCodeUrl: String? = null  // Add this field
)

@Entity(
    tableName = "question_table",
    foreignKeys = [ForeignKey(
        entity = Quiz::class,
        parentColumns = ["id"],
        childColumns = ["quizId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["quizId"])]
)
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val quizId: String,
    val question: String,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String,
    val correctOption: Int
)

data class QuizWithQuestions(
    @Embedded val quiz: Quiz,
    @Relation(
        parentColumn = "id",
        entityColumn = "quizId"
    )
    val questions: List<Question>
)

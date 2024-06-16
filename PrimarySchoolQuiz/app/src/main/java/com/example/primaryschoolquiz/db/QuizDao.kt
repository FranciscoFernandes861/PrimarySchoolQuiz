package com.example.primaryschoolquiz.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface QuizDao {
    @Insert
    suspend fun insertQuiz(quiz: Quiz): Long

    @Insert
    suspend fun insertQuestion(question: Question)

    @Transaction
    @Query("SELECT * FROM quiz_table")
    fun getAllQuizzes(): LiveData<List<QuizWithQuestions>>
}
package com.example.primaryschoolquiz.model.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.primaryschoolquiz.model.Question
import com.example.primaryschoolquiz.model.Quiz
import com.example.primaryschoolquiz.model.QuizWithQuestions
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Insert
    suspend fun insertQuiz(quiz: Quiz): Long

    @Insert
    suspend fun insertQuestion(question: Question)

    @Transaction
    @Query("SELECT * FROM quiz_table WHERE creatorId = :creatorId")
    fun getAllQuizzes(creatorId: String): LiveData<List<QuizWithQuestions>>

    @Transaction
    @Query("SELECT * FROM quiz_table WHERE id = :quizId")
    fun getQuizWithQuestions(quizId: String): Flow<QuizWithQuestions?>
}

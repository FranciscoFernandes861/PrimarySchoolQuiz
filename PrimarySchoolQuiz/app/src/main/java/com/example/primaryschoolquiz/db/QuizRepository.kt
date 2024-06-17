package com.example.primaryschoolquiz.db

import androidx.lifecycle.LiveData

class QuizRepository(private val quizDao: QuizDao) {

    fun getAllQuizzes(userId: String): LiveData<List<QuizWithQuestions>> {
        return quizDao.getAllQuizzes(userId)
    }
    suspend fun insertQuiz(quiz: Quiz): Long {
        return quizDao.insertQuiz(quiz)
    }

    suspend fun insertQuestion(question: Question) {
        quizDao.insertQuestion(question)
    }
}

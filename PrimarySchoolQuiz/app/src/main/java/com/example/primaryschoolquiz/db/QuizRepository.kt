package com.example.primaryschoolquiz.db

import androidx.lifecycle.LiveData

class QuizRepository(private val quizDao: QuizDao) {

    val allQuizzes: LiveData<List<QuizWithQuestions>> = quizDao.getAllQuizzes()

    suspend fun insertQuiz(quiz: Quiz): Long {
        return quizDao.insertQuiz(quiz)
    }

    suspend fun insertQuestion(question: Question) {
        quizDao.insertQuestion(question)
    }
}

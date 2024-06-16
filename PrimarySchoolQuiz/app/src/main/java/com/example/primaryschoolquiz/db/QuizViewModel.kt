package com.example.primaryschoolquiz.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: QuizRepository
    val quizzes: LiveData<List<QuizWithQuestions>>

    init {
        val quizDao = QuizDatabase.getDatabase(application).quizDao()
        repository = QuizRepository(quizDao)
        quizzes = repository.allQuizzes
    }

    fun insertQuiz(quiz: Quiz, onInsertCompleted: (Long) -> Unit) {
        viewModelScope.launch {
            val quizId = repository.insertQuiz(quiz)
            onInsertCompleted(quizId)
        }
    }

    fun insertQuestion(question: Question) {
        viewModelScope.launch {
            repository.insertQuestion(question)
        }
    }
}
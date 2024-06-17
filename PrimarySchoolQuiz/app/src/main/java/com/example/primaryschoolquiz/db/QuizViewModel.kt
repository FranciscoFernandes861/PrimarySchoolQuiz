package com.example.primaryschoolquiz.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: QuizRepository
    val quizzes: LiveData<List<QuizWithQuestions>>

    init {
        val quizDao = QuizDatabase.getDatabase(application).quizDao()
        repository = QuizRepository(quizDao)
        // Assume userId is passed as a parameter or retrieved from a user session
        val userId = getUserIdFromSession()
        quizzes = repository.getAllQuizzes(userId)
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

    private fun getUserIdFromSession(): String {
        // Logic to get the current user ID from the session or authentication system
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }
}

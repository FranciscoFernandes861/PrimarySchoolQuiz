package com.example.primaryschoolquiz.view.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.primaryschoolquiz.model.Question
import com.example.primaryschoolquiz.model.Quiz
import com.example.primaryschoolquiz.model.QuizWithQuestions
import com.example.primaryschoolquiz.model.db.QuizDatabase
import com.example.primaryschoolquiz.model.db.QuizRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: QuizRepository
    val quizzes: LiveData<List<QuizWithQuestions>>

    init {
        val quizDao = QuizDatabase.getDatabase(application).quizDao()
        repository = QuizRepository(quizDao, application)
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
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    fun showQuizCode(quizId: Int) {
    }

    fun submitQuiz(quiz: Quiz, questions: List<Question>, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = repository.submitQuiz(quiz, questions)
            onComplete(result)
        }
    }

    fun getQuizWithQuestions(quizId: String): LiveData<QuizWithQuestions?> {
        println("Fetching quiz with ID: $quizId")
        return repository.getQuizWithQuestions(quizId).asLiveData()
    }
}

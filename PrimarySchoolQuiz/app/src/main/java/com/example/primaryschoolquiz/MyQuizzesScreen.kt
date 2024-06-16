package com.example.primaryschoolquiz

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.primaryschoolquiz.db.QuizViewModel
import com.example.primaryschoolquiz.db.QuizWithQuestions

@Composable
fun MyQuizzesScreen(viewModel: QuizViewModel = viewModel(), navController: NavController? = null) {
    val quizzes by viewModel.quizzes.observeAsState(initial = emptyList())
    var selectedQuiz by remember { mutableStateOf<QuizWithQuestions?>(null) }

    if (selectedQuiz == null) {
        LazyColumn {
            items(quizzes) { quizWithQuestions ->
                QuizItem(quizWithQuestions) {
                    selectedQuiz = quizWithQuestions
                }
            }
        }
    } else {
        QuizDetailScreen(selectedQuiz!!, onBack = { selectedQuiz = null })
    }
}

@Composable
fun QuizItem(quizWithQuestions: QuizWithQuestions, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Text(quizWithQuestions.quiz.name, style = MaterialTheme.typography.headlineSmall)
    }
}

@Composable
fun QuizDetailScreen(quizWithQuestions: QuizWithQuestions, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        TextButton(onClick = onBack) {
            Text("Back")
        }
        Text(quizWithQuestions.quiz.name, style = MaterialTheme.typography.headlineSmall)
        quizWithQuestions.questions.forEach { question ->
            Text(question.question, style = MaterialTheme.typography.bodyMedium)
            Text("1. ${question.option1}")
            Text("2. ${question.option2}")
            Text("3. ${question.option3}")
            Text("4. ${question.option4}")
        }
    }
}

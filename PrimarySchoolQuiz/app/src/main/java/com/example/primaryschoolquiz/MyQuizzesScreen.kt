package com.example.primaryschoolquiz

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.primaryschoolquiz.db.QuizViewModel
import com.example.primaryschoolquiz.db.QuizWithQuestions
import com.example.primaryschoolquiz.ui.CustomTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyQuizzesScreen(viewModel: QuizViewModel = viewModel(), navController: NavController? = null) {
    val quizzes by viewModel.quizzes.observeAsState(initial = emptyList())
    var selectedQuiz by remember { mutableStateOf<QuizWithQuestions?>(null) }
    var showQRCode by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "My Quizzes",
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController?.navigate("teacher_menu")
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                navController = navController!!
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (selectedQuiz == null) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(quizzes) { quizWithQuestions ->
                        QuizItem(
                            quizWithQuestions,
                            onQuizClick = { selectedQuiz = quizWithQuestions },
                            onCodeClick = { showQRCode = quizWithQuestions.quiz.qrCodeUrl }
                        )
                    }
                }
            } else {
                QuizDetailScreen(selectedQuiz!!, onBack = { selectedQuiz = null })
            }

            showQRCode?.let { qrCodeUrl ->
                AlertDialog(
                    onDismissRequest = { showQRCode = null },
                    title = { Text("Quiz Code") },
                    text = {
                        Image(
                            painter = rememberImagePainter(qrCodeUrl),
                            contentDescription = "QR Code"
                        )
                    },
                    confirmButton = {
                        Button(onClick = { showQRCode = null }) {
                            Text("Close")
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun QuizItem(
    quizWithQuestions: QuizWithQuestions,
    onQuizClick: () -> Unit,
    onCodeClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(1.dp, Color.Gray)
            .background(Color.White)
            .clickable(onClick = onQuizClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(quizWithQuestions.quiz.name, style = MaterialTheme.typography.headlineSmall)
        Button(
            onClick = onCodeClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A3A60))
        ) {
            Text("Quiz Code")
        }
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
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

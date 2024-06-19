package com.example.primaryschoolquiz.view.screen

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.primaryschoolquiz.view.viewmodel.QuizViewModel
import com.example.primaryschoolquiz.model.QuizWithQuestions
import com.example.primaryschoolquiz.view.ui.navigation.CustomTopAppBar

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
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
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
                            painter = rememberAsyncImagePainter(qrCodeUrl),
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
            .border(2.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable(onClick = onQuizClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = quizWithQuestions.quiz.name,
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color(0xFF2A3A60),
                fontWeight = FontWeight.Bold
            )
        )
        Button(
            onClick = onCodeClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A3A60))
        ) {
            Text("Quiz Code", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun QuizDetailScreen(quizWithQuestions: QuizWithQuestions, onBack: () -> Unit) {
    var currentIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        val question = quizWithQuestions.questions[currentIndex]
        Text(
            text = question.question,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF2A3A60),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp // Increased the size of the question text
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OptionItem(text = question.option1)
            Spacer(modifier = Modifier.height(8.dp))
            OptionItem(text = question.option2)
            Spacer(modifier = Modifier.height(8.dp))
            OptionItem(text = question.option3)
            Spacer(modifier = Modifier.height(8.dp))
            OptionItem(text = question.option4)
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            if (currentIndex > 0) {
                Button(
                    onClick = { currentIndex-- },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7CD23)),
                    modifier = Modifier.size(160.dp, 48.dp)
                ) {
                    Text("PREVIOUS", color = Color.White, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }
            } else {
                Spacer(modifier = Modifier.size(160.dp, 48.dp))
            }
            if (currentIndex < quizWithQuestions.questions.size - 1) {
                Button(
                    onClick = { currentIndex++ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7CD23)),
                    modifier = Modifier.size(160.dp, 48.dp)
                ) {
                    Text("NEXT", color = Color.White, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }
            } else {
                Spacer(modifier = Modifier.size(160.dp, 48.dp))
            }
        }
    }
}

@Composable
fun OptionItem(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2A3A60), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(text, color = Color.White, style = MaterialTheme.typography.bodyMedium)
    }
}

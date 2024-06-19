package com.example.primaryschoolquiz.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.primaryschoolquiz.view.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizDetailScreen(viewModel: QuizViewModel, quizId: String, onBack: () -> Unit) {
    val quizWithQuestions by viewModel.getQuizWithQuestions(quizId).observeAsState()

    LaunchedEffect(quizWithQuestions) {
        println("QuizWithQuestions state updated: $quizWithQuestions")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color.White),
            contentAlignment = Alignment.Center) {

            if (quizWithQuestions == null) {
                CircularProgressIndicator()
                Text("Loading quiz...", color = Color.Gray, fontSize = 16.sp)
            } else {
                var currentIndex by remember { mutableStateOf(0) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .background(Color.White)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    val question = quizWithQuestions!!.questions[currentIndex]
                    Text(
                        text = question.question,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color(0xFF2A3A60),
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
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
                        }
                        if (currentIndex < quizWithQuestions!!.questions.size - 1) {
                            Button(
                                onClick = { currentIndex++ },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7CD23)),
                                modifier = Modifier.size(160.dp, 48.dp)
                            ) {
                                Text("NEXT", color = Color.White, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
        }
    }
}

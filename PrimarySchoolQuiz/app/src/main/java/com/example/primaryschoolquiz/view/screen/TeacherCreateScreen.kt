package com.example.primaryschoolquiz.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.primaryschoolquiz.model.Question
import com.example.primaryschoolquiz.model.Quiz
import com.example.primaryschoolquiz.view.viewmodel.QuizViewModel
import com.example.primaryschoolquiz.view.ui.navigation.CustomTopAppBar
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuizScreen(viewModel: QuizViewModel = viewModel(), navController: NavController? = null) {
    var step by remember { mutableStateOf(1) }
    var quizName by remember { mutableStateOf("") }
    val questionState = remember { mutableStateOf("") }
    val option1State = remember { mutableStateOf("") }
    val option2State = remember { mutableStateOf("") }
    val option3State = remember { mutableStateOf("") }
    val option4State = remember { mutableStateOf("") }
    val correctOptionState = remember { mutableStateOf(1) }
    val questions = remember { mutableStateListOf<Question>() }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Create Quiz",
                navigationIcon = {
                    IconButton(
                        onClick = { navController?.navigateUp() }
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
                .padding(innerPadding)
                .background(Color.White)
                .padding(16.dp)
        ) {
            when (step) {
                1 -> {
                    // Step 1: Enter Quiz Name
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TextField(
                            value = quizName,
                            onValueChange = { quizName = it },
                            label = { Text("Quiz Name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(8.dp))
                                .padding(16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (quizName.isNotEmpty()) {
                                    step = 2
                                } else {
                                    showDialog = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7CD23))
                        ) {
                            Text("Next", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                2 -> {
                    // Step 2: Enter Questions and Options
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TextField(
                            value = questionState.value,
                            onValueChange = { questionState.value = it },
                            label = { Text("Question") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(8.dp))
                                .padding(16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = option1State.value,
                            onValueChange = { option1State.value = it },
                            label = { Text("Option 1") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(8.dp))
                                .padding(16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = option2State.value,
                            onValueChange = { option2State.value = it },
                            label = { Text("Option 2") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(8.dp))
                                .padding(16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = option3State.value,
                            onValueChange = { option3State.value = it },
                            label = { Text("Option 3") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(8.dp))
                                .padding(16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = option4State.value,
                            onValueChange = { option4State.value = it },
                            label = { Text("Option 4") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(8.dp))
                                .padding(16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                val question = Question(
                                    quizId = "0",  // Placeholder, will be set correctly after quiz insertion
                                    question = questionState.value,
                                    option1 = option1State.value,
                                    option2 = option2State.value,
                                    option3 = option3State.value,
                                    option4 = option4State.value,
                                    correctOption = correctOptionState.value
                                )
                                questions.add(question)
                                questionState.value = ""
                                option1State.value = ""
                                option2State.value = ""
                                option3State.value = ""
                                option4State.value = ""
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7CD23)),
                            enabled = quizName.isNotEmpty()
                        ) {
                            Text("Add Next Question", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (questions.isNotEmpty()) {
                                    val quiz = Quiz(name = quizName, creatorId = userId)
                                    viewModel.submitQuiz(quiz, questions) { success ->
                                        if (success) {
                                            navController?.navigate("my_quizzes")
                                        } else {
                                            // Handle failure
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7CD23)),
                            enabled = questions.isNotEmpty()
                        ) {
                            Text("Submit Quiz", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Error") },
                    text = { Text("Quiz name cannot be empty.") },
                    confirmButton = {
                        Button(
                            onClick = { showDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7CD23))
                        ) {
                            Text("OK", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
        }
    }
}

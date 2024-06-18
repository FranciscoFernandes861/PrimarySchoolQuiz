package com.example.primaryschoolquiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.primaryschoolquiz.db.Quiz
import com.example.primaryschoolquiz.db.Question
import com.example.primaryschoolquiz.db.QuizViewModel

@Composable
fun CreateQuizScreen(viewModel: QuizViewModel, navController: NavController? = null) {
    var step by remember { mutableStateOf(1) }
    var quizId by remember { mutableStateOf<Long?>(null) }
    var quizName by remember { mutableStateOf("") }
    val questionState = remember { mutableStateOf("") }
    val option1State = remember { mutableStateOf("") }
    val option2State = remember { mutableStateOf("") }
    val option3State = remember { mutableStateOf("") }
    val option4State = remember { mutableStateOf("") }
    val correctOptionState = remember { mutableStateOf(1) }

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
                    label = { Text("Quiz Name") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    val quiz = Quiz(name = quizName)
                    viewModel.insertQuiz(quiz) { newQuizId ->
                        quizId = newQuizId
                        step = 2
                    }
                }) {
                    Text("Next")
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
                    label = { Text("Question") }
                )
                TextField(
                    value = option1State.value,
                    onValueChange = { option1State.value = it },
                    label = { Text("Option 1") }
                )
                TextField(
                    value = option2State.value,
                    onValueChange = { option2State.value = it },
                    label = { Text("Option 2") }
                )
                TextField(
                    value = option3State.value,
                    onValueChange = { option3State.value = it },
                    label = { Text("Option 3") }
                )
                TextField(
                    value = option4State.value,
                    onValueChange = { option4State.value = it },
                    label = { Text("Option 4") }
                )
                // Add selection for correct option (dropdown or radio buttons)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    val question = Question(
                        quizId = quizId!!.toInt(),
                        question = questionState.value,
                        option1 = option1State.value,
                        option2 = option2State.value,
                        option3 = option3State.value,
                        option4 = option4State.value,
                        correctOption = correctOptionState.value
                    )
                    viewModel.insertQuestion(question)
                    questionState.value = ""
                    option1State.value = ""
                    option2State.value = ""
                    option3State.value = ""
                    option4State.value = ""
                }) {
                    Text("Add Next Question")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    navController?.navigate("my_quizzes")
                }) {
                    Text("Submit Quiz")
                }
            }
        }
    }
}

package com.example.primaryschoolquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.FirebaseApp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import com.example.primaryschoolquiz.ui.theme.PrimarySchoolQuizTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.primaryschoolquiz.db.QuizViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            PrimarySchoolQuizTheme {
                AppQuiz()
            }
        }
    }

    @Composable
    fun AppQuiz() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                InitialScreen(navController)
            }
            composable("teacher_login") {
                TeacherLoginScreen(navController)
            }
            composable("sign_up") {
                SignUpScreen(navController)
            }
            composable("teacher_profile") {
                TeacherProfileScreen(navController)
            }
            composable("teacher_app") {
                TeacherApplicationScreen(navController)
            }
            composable("teacher_menu") {
                TeacherMenuScreen(navController) { // Implement the logout functionality
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("home") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            }
            composable("create_quiz") {
                val viewModel: QuizViewModel = viewModel()
                CreateQuizScreen(viewModel, navController)
            }
            composable("my_quizzes") {
                val viewModel: QuizViewModel = viewModel()
                MyQuizzesScreen(viewModel, navController)
            }
            composable("teachers") {
                val viewModel: QuizViewModel = viewModel()
                CreateQuizScreen(viewModel, navController)
            }
        }
    }

    @Preview
    @Composable
    fun InitialScreen(navController: NavController? = null) {
        ImageNameAndButton(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFE35C), // Cor inicial
                            Color(0xFFFFE35C),
                            Color(0xFFFFDA4B), // Cores do meio
                            Color(0xFFFFDA4B),
                            Color(0xFFFFDA4B)  // Cor final
                        )
                    )
                ),
            navController = navController
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ImageNameAndButton(modifier: Modifier = Modifier, navController: NavController? = null) {
        val textState = remember { mutableStateOf("") }

        Box(
            modifier = modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(18.dp)
                    .zIndex(1f)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_login_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(55.dp)
                        .offset(x = 15.dp)
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(color = Color.White),
                            onClick = {
                                navController?.navigate("teacher_login")
                            }
                        )
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "Teacher's Login",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF213056)
                    )
                )
            }

            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_primary_school_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(320.dp)
                        .offset(y = (-70).dp) // Faz subir a imagem em relação ao centro
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "What is your name?",
                    style = TextStyle(
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

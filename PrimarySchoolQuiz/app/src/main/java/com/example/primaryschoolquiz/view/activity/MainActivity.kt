package com.example.primaryschoolquiz.view.activity

import com.example.primaryschoolquiz.view.screen.MapScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.primaryschoolquiz.view.screen.ChallengeModeScreen
import com.example.primaryschoolquiz.view.screen.CreateQuizScreen
import com.example.primaryschoolquiz.view.screen.MyQuizzesScreen
import com.example.primaryschoolquiz.view.ui.utils.QRCodeScanner
import com.example.primaryschoolquiz.view.screen.QuizDetailScreen
import com.example.primaryschoolquiz.view.screen.SignUpScreen
import com.example.primaryschoolquiz.view.screen.TeacherApplicationScreen
import com.example.primaryschoolquiz.view.screen.TeacherLoginScreen
import com.example.primaryschoolquiz.view.screen.TeacherMenuScreen
import com.example.primaryschoolquiz.view.screen.TeacherProfileScreen
import com.example.primaryschoolquiz.view.ui.utils.TiltTask
import com.example.primaryschoolquiz.view.viewmodel.QuizViewModel
import com.example.primaryschoolquiz.view.screen.InitialScreen
import com.example.primaryschoolquiz.view.ui.theme.PrimarySchoolQuizTheme
import com.google.firebase.FirebaseApp
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
                TeacherMenuScreen(navController) {
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
            composable("movement_challenge") {
                ChallengeModeScreen(navController)
            }
            composable("tilt_task") {
                TiltTask(navController)
            }
            composable("enter_qr_code") {
                QRCodeScanner(navController) { scannedResult ->
                    navController.navigate("quiz_detail/$scannedResult")
                }
            }
            composable("quiz_detail/{quizId}") { backStackEntry ->
                val quizId = backStackEntry.arguments?.getString("quizId") ?: return@composable
                val viewModel: QuizViewModel = viewModel()
                QuizDetailScreen(viewModel = viewModel, quizId = quizId, onBack = { navController.popBackStack() })
            }
            composable("map_screen") {
                MapScreen(navController)
            }
        }
    }
}

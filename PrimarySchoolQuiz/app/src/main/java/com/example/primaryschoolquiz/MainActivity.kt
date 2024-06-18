package com.example.primaryschoolquiz

import QRCodeScanner
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.FirebaseApp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.example.primaryschoolquiz.ui.theme.PrimarySchoolQuizTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.primaryschoolquiz.db.QuizViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

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
            composable("teacher_menu") {
                TeacherMenuScreen(navController)
            }
            composable("create_quiz") {
                val viewModel: QuizViewModel = viewModel()
                CreateQuizScreen(viewModel, navController)
            }
            composable("my_quizzes") {
                val viewModel: QuizViewModel = viewModel()
                MyQuizzesScreen(viewModel)
            }
            composable("movement_challenge") {
                ChallengeModeScreen(navController)
            }
            composable("tilt_task") {
                TiltTask(navController)
            }
            composable("enter_qr_code") {
                QRCodeScanner(){}
                //onQRCodeScanned = {qrCode ->
                // Tratar de Scan Qr code
                //},
                //onDismiss = {
                //    navController.popBackStack()
                //}

                }
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
        val showDialog = remember { mutableStateOf(false) }
        val showSubDialog = remember { mutableStateOf(false) }
        val showCodeInputDialog = remember { mutableStateOf(false) }

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
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TextField(
                    value = textState.value,
                    onValueChange = { newValue ->
                        if (newValue.length <= 25) {
                            textState.value = newValue
                        }
                    },
                    label = { Text("Enter your name") },
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = Color(0xFFC3C3C3),
                        unfocusedLabelColor = Color.Black
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .offset(y = (20).dp) //Offset para subir textField
                        .padding(horizontal = 20.dp)
                        .border(BorderStroke(3.dp, Color(0xFF213056))),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = androidx.compose.ui.text.input.ImeAction.Done
                    )
                )

            }
            Button(
                onClick = { showDialog.value = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF213056)),
                modifier = Modifier
                    .size(width = 170.dp, height = 60.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = (-70).dp) // Offset para subir botão play
            ) {
                Text(
                    text = stringResource(R.string.play),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) // botão play
            }
        }
        if (showDialog.value) {
            GameOptionsDialog(
                onDismiss = { showDialog.value = false },
                onScanQuizSelected = {
                    showDialog.value = false // Tirar diálogo atual
                    showSubDialog.value = true
                },
                onMovementChallengeSelected = {
                    navController?.navigate("movement_challenge")
                }
            )
        }

        if (showSubDialog.value) {
            EnterQuizDialog(
                onDismiss = { showSubDialog.value = false },
                onCodeSelected = {
                    showSubDialog.value = false // Tirar diálogo atual
                    showCodeInputDialog.value = true
                },
                onQrSelected = {
                    showSubDialog.value = false
                    navController?.navigate("enter_qr_code")
                }
            )
        }

        if (showCodeInputDialog.value) {
            EnterCodeDialog(
                onDismiss = { showCodeInputDialog.value = false },
                onSubmit = { code ->
                    showCodeInputDialog.value = false
                    // Handle the submitted code
                }
            )
        }
    }
        @Composable
        fun GameOptionsDialog(
            onDismiss: () -> Unit,
            onScanQuizSelected: () -> Unit,
            onMovementChallengeSelected: () -> Unit
        ) {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = {
                    Text(text = "Choose an Option",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 23.sp,
                        ),
                        modifier = Modifier.offset(x = 30.dp)
                        )
                },

                text = {
                    Text("Select your gameplay mode:",
                        modifier = Modifier.offset(y = 10.dp)
                        )
                },
                confirmButton = {
                    Column {
                        TextButton(onClick = {
                            onDismiss()
                            onScanQuizSelected()
                        },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF213056)),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .offset(x = -(35).dp)
                            ) {
                            Text("Start Quiz")
                        }
                        TextButton(onClick = {
                            onDismiss()
                            onMovementChallengeSelected()
                        },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7CD23)),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .offset(x = -(35).dp)
                            ) {
                            Text("Movement Challenge")
                        }
                    }
                }
            )
        }


        @Composable
        fun EnterQuizDialog(
            onDismiss: () -> Unit,
            onCodeSelected: () -> Unit,
            onQrSelected: () -> Unit
        ) {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = {
                    Text(text = "Enter Quiz",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 23.sp,
                        ),
                        modifier = Modifier.offset(x = 60.dp)
                    )
                },
                text = {
                    Text("Choose an entry method:",
                        modifier = Modifier.offset(y = 10.dp)
                    )
                },
                confirmButton = {
                    Column {
                        TextButton(onClick = {
                            onDismiss()
                            onCodeSelected()
                        },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF213056)),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .offset(x = -(70).dp)
                            ) {
                            Text("Quiz Code")
                        }
                        TextButton(onClick = {
                            onDismiss()
                            onQrSelected()
                        },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7CD23)),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .offset(x = -(70).dp)
                            ) {
                            Text("QR Code")
                        }
                    }
                }
            )
        }

        @Composable
        fun EnterCodeDialog(
            onDismiss: () -> Unit,
            onSubmit: (String) -> Unit
        ) {
            val code = remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = onDismiss,
                title = {
                    Text(text = "Enter Quiz Code",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 23.sp,
                        ),
                        )
                },
                text = {
                    Column {
                        Text("Please enter your quiz code below:")
                        Spacer(modifier = Modifier.height(10.dp))
                        TextField(
                            value = code.value,
                            onValueChange = { code.value = it },
                            label = { Text("Quiz Code") },
                            colors = TextFieldDefaults.colors(
                                focusedLabelColor = Color(0xFFC3C3C3),
                                unfocusedLabelColor = Color.Black
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(BorderStroke(3.dp, Color(0xFF213056))),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                imeAction = androidx.compose.ui.text.input.ImeAction.Done
                            )
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        onDismiss()
                        onSubmit(code.value)
                    },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF213056)),
                        ) {
                        Text("Go")
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                }
            )
        }
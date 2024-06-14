package com.example.primaryschoolquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.modifier.ModifierLocalMap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                SignUpScreen()
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

        val textState = remember{ mutableStateOf("") }

        Box(
            modifier = modifier
                .fillMaxSize()
        ){

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
                        .offset(x = 12.dp)
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = rememberRipple(color = Color.White),
                            onClick = {
                                navController?.navigate("teacher_login")
                            }
                        )
                )
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
            modifier = modifier.fillMaxSize(), // ir buscar modifier
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
            ) {
            Image(
                painter = painterResource(R.drawable.ic_primary_school_logo),
                null,
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
                onClick = { /*TODO*/ },
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
}
}

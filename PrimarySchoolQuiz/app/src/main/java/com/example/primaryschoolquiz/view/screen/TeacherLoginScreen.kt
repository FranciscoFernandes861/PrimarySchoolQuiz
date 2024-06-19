package com.example.primaryschoolquiz.view.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import com.example.primaryschoolquiz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

@Composable
fun TeacherLoginScreen(navController: NavController? = null) {

    val emailState = remember {
        mutableStateOf(TextFieldValue(""))
    }

    val passwordState = remember {
        mutableStateOf(TextFieldValue(""))
    }

    val context = LocalContext.current

    fun login(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navController?.navigate("teacher_app")
                } else {
                    // Handle errors
                    try {
                        throw task.exception ?: Exception("Login failed")
                    } catch (e: FirebaseAuthInvalidUserException) {
                        Toast.makeText(context, "User not found. Please sign up first.", Toast.LENGTH_LONG).show()
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(context, "Invalid password. Please try again.", Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Login failed: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF213056))
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (150).dp)
        ) {
            Card (
                modifier = Modifier
                    .size(140.dp)
                    .shadow(16.dp, RoundedCornerShape(24.dp), clip = false)
                    .clip(RoundedCornerShape(24.dp)),

                //colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Image(painter = painterResource(R.drawable.ic_primary_school_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                    //.padding(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Teacher's Login",
                style = TextStyle(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),

            )
            Spacer(modifier = Modifier.height(32.dp))
            Column (
                modifier = Modifier
                    .offset(y = 20.dp)
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth()
            ) {
                TextField(
                    value = emailState.value,
                    onValueChange = { emailState.value = it },
                    label = { Text("Email") },
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = Color.Gray,
                        unfocusedLabelColor = Color.Black
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = passwordState.value,
                    onValueChange = { passwordState.value = it },
                    label = { Text("Password") },
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = Color.Gray,
                        unfocusedLabelColor = Color.Black
                    ),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Sign up",
                    style = TextStyle(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier
                        .offset(x = 255.dp)
                        .offset(y = 6.dp)
                        .clickable {
                            navController?.navigate("sign_up")
                        }
                )
            }
        }
        Button(
            onClick = {
                login(emailState.value.text, passwordState.value.text)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(100.dp)
                .offset(y = 30.dp)
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text(
                text = "Login",
                color = Color(0xFF213056),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
        IconButton(
            onClick = {
                navController?.navigate("home"){
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
          },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .size(48.dp)
                    .background(Color.White, CircleShape)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
        }
    }
}

@Preview
@Composable
fun PreviewTeacherLoginScreen() {
    TeacherLoginScreen()
}
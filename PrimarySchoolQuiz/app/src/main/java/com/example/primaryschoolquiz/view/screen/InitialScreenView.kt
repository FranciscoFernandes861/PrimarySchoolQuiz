package com.example.primaryschoolquiz.view.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.primaryschoolquiz.R

@Composable
fun InitialScreen(navController: NavController? = null) {
    val showDialog = remember { mutableStateOf(false) }
    val showSubDialog = remember { mutableStateOf(false) }
    val showCodeInputDialog = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFE35C), // Initial color
                        Color(0xFFFFE35C),
                        Color(0xFFFFDA4B), // Middle colors
                        Color(0xFFFFDA4B),
                        Color(0xFFFFDA4B)  // Final color
                    )
                )
            )
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
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp), // Add padding to center the content better
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_primary_school_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(320.dp)
                    .offset(y = (-30).dp) // Adjust position
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { showDialog.value = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF213056)),
                modifier = Modifier.size(width = 170.dp, height = 60.dp)
            ) {
                Text(
                    text = stringResource(R.string.play).uppercase(),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                ) // Play button
            }
        }

        if (showDialog.value) {
            GameOptionsDialog(
                onDismiss = { showDialog.value = false },
                onScanQuizSelected = {
                    showDialog.value = false // Dismiss current dialog
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
                    showSubDialog.value = false // Dismiss current dialog
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "CHOOSE AN OPTION",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 23.sp,
                        color = Color(0xFF213056)
                    )
                )
            }
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Select your gameplay mode:",
                    color = Color(0xFF213056)
                )
            }
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(
                    onClick = {
                        onDismiss()
                        onScanQuizSelected()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF213056)),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("START QUIZ",
                        color = Color.White,
                        fontWeight = FontWeight.Bold)
                }
                TextButton(
                    onClick = {
                        onDismiss()
                        onMovementChallengeSelected()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7CD23)),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("MOVEMENT CHALLENGE",
                        color = Color.White,
                        fontWeight = FontWeight.Bold)
                }
            }
        },
        containerColor = Color.White
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ENTER QUIZ",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 23.sp,
                        color = Color(0xFF213056)
                    )
                )
            }
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Choose an entry method:",
                    color = Color(0xFF213056)
                )
            }
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(
                    onClick = {
                        onDismiss()
                        onCodeSelected()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF213056)),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("QUIZ CODE",
                        color = Color.White,
                        fontWeight = FontWeight.Bold)
                }
                TextButton(
                    onClick = {
                        onDismiss()
                        onQrSelected()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7CD23)),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("QR CODE",
                        color = Color.White,
                        fontWeight = FontWeight.Bold)
                }
            }
        },
        containerColor = Color.White
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ENTER QUIZ CODE",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 23.sp,
                        color = Color(0xFF213056)
                    )
                )
            }
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Please enter your quiz code below:", color = Color(0xFF213056))
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
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    onSubmit(code.value)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF213056)),
            ) {
                Text("GO", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = Color(0xFF213056), fontWeight = FontWeight.Bold)
            }
        },
        containerColor = Color.White
    )
}

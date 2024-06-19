package com.example.primaryschoolquiz.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.primaryschoolquiz.view.ui.navigation.CustomTopAppBar
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherApplicationScreen(navController: NavController? = null) {
    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email ?: "No Email"

    Scaffold(
        topBar = {
            if (navController != null) {
                CustomTopAppBar(
                    title = "Teacher Application",
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.navigate("teacher_menu")
                            }
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                        }
                    },
                    navController = navController
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Welcome to the Teacher Application", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                navController?.navigate("map_screen")
            }) {
                Text("Show Map")
            }
        }
    }
}


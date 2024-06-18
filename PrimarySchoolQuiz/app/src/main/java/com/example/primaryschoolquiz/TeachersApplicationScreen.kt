package com.example.primaryschoolquiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.primaryschoolquiz.ui.CustomTopAppBar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherApplicationScreen(navController: NavController? = null) {
    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email ?: "No Email"
    val scope = rememberCoroutineScope()

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Welcome to the Teacher Application", fontSize = 18.sp)
        }
    }
}

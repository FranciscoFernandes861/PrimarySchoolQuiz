package com.example.primaryschoolquiz

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.example.primaryschoolquiz.ui.CustomTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherMenuScreen(navController: NavController, onLogout: () -> Unit) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Teacher Menu",
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate("teacher_app")
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                navController = navController
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                DrawerItem(
                    icon = Icons.Default.Create,
                    label = "Create Quiz",
                    onClick = {
                        navController.navigate("create_quiz")
                    }
                )
                DrawerItem(
                    icon = Icons.Default.List,
                    label = "My Quizzes",
                    onClick = {
                        navController.navigate("my_quizzes")
                    }
                )
                DrawerItem(
                    icon = Icons.Default.Person,
                    label = "Profile",
                    onClick = {
                        navController.navigate("teacher_profile")
                    }
                )
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Divider(color = Color(0xFF213056), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                DrawerItem(
                    icon = Icons.Default.ExitToApp,
                    label = "Logout",
                    onClick = { onLogout() }
                )
            }
        }
    }
}

@Composable
fun DrawerItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF213056))
            .padding(12.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        androidx.compose.material3.Icon(icon, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(16.dp))
        androidx.compose.material3.Text(label, style = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 18.sp))
    }
}

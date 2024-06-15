package com.example.primaryschoolquiz

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherMenuScreen(navController: NavController? = null) {
    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email ?: "No Email"
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        navController?.navigate("home") {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF213056),
                                Color(0xFF2A3A60)
                            )
                        )
                    )
                    .fillMaxSize()
                    .shadow(8.dp, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Teacher Menu",
                            style = TextStyle(
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF213056)
                            ),
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                        Divider(color = Color(0xFF213056), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                        DrawerItem(
                            icon = Icons.Default.Create,
                            label = "Create Quiz",
                            onClick = {
                                // Navigate to Create Quiz Screen
                            }
                        )
                        DrawerItem(
                            icon = Icons.Default.List,
                            label = "My Quizzes",
                            onClick = {
                                // Navigate to My Quizzes Screen
                            }
                        )
                        DrawerItem(
                            icon = Icons.Default.Person,
                            label = "Profile",
                            onClick = {
                                navController?.navigate("teacher_profile")
                            }
                        )
                    }
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Divider(color = Color(0xFF213056), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                        DrawerItem(
                            icon = Icons.Default.ExitToApp,
                            label = "Logout",
                            onClick = { logout() }
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Teacher Application") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF213056),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Welcome to the Teacher Application")
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
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(16.dp))
        Text(label, style = TextStyle(color = Color.White, fontSize = 18.sp))
    }
}

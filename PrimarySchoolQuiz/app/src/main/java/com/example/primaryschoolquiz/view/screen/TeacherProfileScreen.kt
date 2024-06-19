package com.example.primaryschoolquiz.view.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.primaryschoolquiz.view.ui.navigation.CustomTopAppBar
import com.google.accompanist.permissions.*
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.location.LocationServices

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TeacherProfileScreen(navController: NavController? = null) {
    val user = FirebaseAuth.getInstance().currentUser
    val email = remember { mutableStateOf(user?.email ?: "No Email") }
    val context = LocalContext.current
    var location by remember { mutableStateOf<Location?>(null) }
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        navController?.navigate("home") {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun requestLocation() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { loc: Location? ->
                    location = loc
                }
        } else {
            locationPermissionState.launchPermissionRequest()
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Teacher Profile",
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController?.navigate("teacher_menu")
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                navController = navController!!
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF213056)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    text = "Teacher Profile",
                    style = TextStyle(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Email: ${email.value}",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                location?.let {
                    Text(
                        text = "Location: (${it.latitude}, ${it.longitude})",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { logout() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "Logout",
                        color = Color(0xFF213056),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { requestLocation() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "Show Your Location",
                        color = Color(0xFF213056),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewTeacherProfileScreen() {
    val mockNavController = rememberNavController()
    TeacherProfileScreen(navController = mockNavController)
}

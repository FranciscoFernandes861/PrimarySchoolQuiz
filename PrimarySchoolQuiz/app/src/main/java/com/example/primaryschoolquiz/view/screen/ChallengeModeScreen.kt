package com.example.primaryschoolquiz.view.screen

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.math.sqrt

@Composable
fun ChallengeModeScreen(navController: NavController? = null) {

    var shakeCount by remember { mutableIntStateOf(0) }
    var winMessage by remember { mutableStateOf("") }
    var solidColor by remember { mutableStateOf<Color?>(null) }
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (winMessage.isEmpty()) {
                    event?.let {
                        val x = it.values[0]
                        val y = it.values[1]
                        val z = it.values[2]

                        val acceleration = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH
                        if (acceleration > 30) {
                            shakeCount++
                            if (shakeCount >= 5) {
                                solidColor = listOf(
                                    Color.Red,
                                    Color.Green,
                                    Color.Yellow
                                ).random()
                                winMessage = "TASK COMPLETED!"
                                shakeCount = 0 // Reset the counter after winning
                            }
                        }
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_UI)

        onDispose {
            sensorManager.unregisterListener(sensorListener)
        }
    }

    val backgroundModifier = if (solidColor != null) {
        Modifier.background(solidColor!!)
    } else {
        Modifier.background(
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
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(backgroundModifier),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (winMessage.isNotEmpty()) {
                Text(
                    text = winMessage,
                    color = Color(0xFF213056),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { navController?.navigate("tilt_task") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF213056)),
                    modifier = Modifier.size(width = 200.dp, height = 60.dp)
                ) {
                    Text(
                        text = "NEXT TASK",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Text(
                    text = "SHAKE THE PHONE",
                    color = Color(0xFF213056),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

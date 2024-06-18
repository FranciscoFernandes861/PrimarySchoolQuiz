package com.example.primaryschoolquiz

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TiltTask(navController: NavController? = null) {
    var ballX by remember { mutableFloatStateOf(0f) }
    var ballY by remember { mutableFloatStateOf(0f) }
    var isTaskCompleted by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val targetX = 10f // Example target position
    val targetY = -300f // Example target position
    val ballSize = 50.dp
    val targetSize = 50.dp

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (!isTaskCompleted) {
                    event?.let {
                        val x = it.values[0]
                        val y = it.values[1]

                        // Update ball position based on tilt
                        ballX -= x
                        ballY += y

                        // Boundaries check to keep the ball within the screen limits
                        ballX = ballX.coerceIn(-200f, 200f) // Adjust according to your layout
                        ballY = ballY.coerceIn(-350f, 350f) // Adjust according to your layout
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

    Box(
        modifier = Modifier
            .fillMaxSize()
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
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(ballSize)
                .offset(x = ballX.dp, y = ballY.dp)
                .background(Color.Red, shape = CircleShape)
        )
        Box(
            modifier = Modifier
                .size(targetSize)
                .offset(x = targetX.dp, y = targetY.dp)
                .background(Color.Green, shape = CircleShape)
        )
        val halfBallSize = ballSize / 2
        val halfTargetSize = targetSize / 2

        if (ballX in (targetX - halfTargetSize.value)..(targetX + halfTargetSize.value) &&
            ballY in (targetY - halfTargetSize.value)..(targetY + halfTargetSize.value)
        ) {
            isTaskCompleted = true
            Text(
                "Challenge Mode Completed!",
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )
            Button(
                onClick = { navController?.navigate("home") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF213056)),
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 60.dp)
            ) {
                Text("Return to start menu")
            }
        } else {
            Text(
                "Make the red ball reach the green one!",
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )
            Text(
                "Tilt your phone!",
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 30.dp)
            )
        }
    }
}

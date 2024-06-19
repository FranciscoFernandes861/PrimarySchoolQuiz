package com.example.primaryschoolquiz.view.ui.utils

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRCodeScanner(navController: NavController, onQRCodeScanned: (String) -> Unit) {
    val context = LocalContext.current
    val activity = context as? Activity
    val scannedResult = rememberSaveable { mutableStateOf<String?>(null) }

    val qrCodeScannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val intentResult: IntentResult? = IntentIntegrator.parseActivityResult(result.resultCode, result.data)
        if (intentResult != null && intentResult.contents != null) {
            println("Scanned QR Code: ${intentResult.contents}")
            scannedResult.value = intentResult.contents
            onQRCodeScanned(intentResult.contents)
            navController.navigate("quiz_detail/${intentResult.contents}")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QR Scanner") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        activity?.let {
                            IntentIntegrator(it).apply {
                                setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                                setPrompt("Scan QR code")
                                setCameraId(0)
                                setBeepEnabled(false)
                                setBarcodeImageEnabled(true)
                                qrCodeScannerLauncher.launch(createScanIntent())
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                    modifier = Modifier.size(width = 200.dp, height = 60.dp)
                ) {
                    Text(text = "Scan QR Code", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                scannedResult.value?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Scanned QR Code: $it")
                }
            }
        }
    )
}

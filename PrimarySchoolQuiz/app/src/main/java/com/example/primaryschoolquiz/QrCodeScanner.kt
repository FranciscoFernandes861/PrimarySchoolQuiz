import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

@Composable
fun QRCodeScanner(onQRCodeScanned: (String) -> Unit) {
    val context = LocalContext.current
    val activity = context as? Activity
    val scannedResult = rememberSaveable { mutableStateOf<String?>(null) }

    val qrCodeScannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val intentResult: IntentResult? = IntentIntegrator.parseActivityResult(result.resultCode, result.data)
        if (intentResult != null && intentResult.contents != null) {
            scannedResult.value = intentResult.contents
            onQRCodeScanned(intentResult.contents)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
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
        }) {
            Text(text = "Scan QR Code")
        }

        scannedResult.value?.let {
            Text(text = "Scanned QR Code: $it")
        }
    }
}

package bruhcollective.itaysonlab.jetisteam.guard.qr_code

import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
internal fun CameraView(
    modifier: Modifier = Modifier,
    handler: (Barcode?) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cxController = remember { LifecycleCameraController(context) }
    val cxView = remember { PreviewView(context) }

    val cxBarcodeExecutor = remember { Dispatchers.Default.limitedParallelism(2).asExecutor() }

    val cxBarcodeScanner = remember { BarcodeScanning.getClient(BarcodeScannerOptions.Builder().setBarcodeFormats(
        Barcode.FORMAT_QR_CODE
    ).setExecutor(cxBarcodeExecutor).build()) }

    LaunchedEffect(Unit) {
        cxController.setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
        cxController.bindToLifecycle(lifecycleOwner)

        cxController.setImageAnalysisAnalyzer(cxBarcodeExecutor, MlKitAnalyzer(listOf(
            cxBarcodeScanner
        ), ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED, cxBarcodeExecutor) { result ->
            handler(result.getValue(cxBarcodeScanner)?.firstOrNull())
        })

        cxView.controller = cxController
    }

    DisposableEffect(Unit) {
        onDispose {
            cxView.controller = null
            cxController.unbind()
        }
    }

    Box(modifier = modifier.background(Color.Black)) {
        AndroidView(factory = { cxView }, modifier = Modifier.fillMaxSize())
    }
}
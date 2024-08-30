package bruhcollective.itaysonlab.cobalt.guard.qr_code

import android.view.ViewGroup
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    val lifecycleOwner = LocalLifecycleOwner.current

    Box(modifier = modifier.background(Color.Black)) {
        AndroidView(
            factory = { context ->
                val cxController = LifecycleCameraController(context)
                val cxBarcodeExecutor = Dispatchers.Default.limitedParallelism(2).asExecutor()
                val cxBarcodeScanner = BarcodeScanning.getClient(BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).setExecutor(cxBarcodeExecutor).build())

                cxController.setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
                cxController.bindToLifecycle(lifecycleOwner)
                cxController.setImageAnalysisAnalyzer(cxBarcodeExecutor, MlKitAnalyzer(listOf(cxBarcodeScanner), ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED, cxBarcodeExecutor) { result ->
                    handler(result.getValue(cxBarcodeScanner)?.firstOrNull())
                })

                return@AndroidView PreviewView(context).also { view ->
                    view.controller = cxController
                }
            },
            modifier = Modifier.fillMaxSize(),
            onRelease = {
                (it.controller as? LifecycleCameraController)?.unbind()
                it.controller = null
            }, update = {
                (it.parent as? ViewGroup)?.clipChildren = false
            }
        )
    }
}
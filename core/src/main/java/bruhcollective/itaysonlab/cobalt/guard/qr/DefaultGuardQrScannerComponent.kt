package bruhcollective.itaysonlab.cobalt.guard.qr

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.KoinComponent

internal class DefaultGuardQrScannerComponent (
    componentContext: ComponentContext
): GuardQrScannerComponent, KoinComponent, ComponentContext by componentContext {

}
package bruhcollective.itaysonlab.cobalt.core.decompose

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class ViewModel : InstanceKeeper.Instance {
    protected val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)

    final override fun onDestroy() {
        viewModelScope.cancel()
    }
}
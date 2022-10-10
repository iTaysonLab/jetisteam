package bruhcollective.itaysonlab.microapp.guard.ui.devices

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GuardDevicesScreen(
    viewModel: GuardDevicesViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            LargeTopAppBar(title = {
                Text(text = "Authorized Devices")
            }, navigationIcon = {
                IconButton(onClick = onBackClicked) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                }
            })
        }, contentWindowInsets = EmptyWindowInsets
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .clip(MaterialTheme.shapes.extraLarge.copy(bottomStart = CornerSize(0.dp), bottomEnd = CornerSize(0.dp)))
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
        ) {

        }
    }
}
package bruhcollective.itaysonlab.microapp.guard.ui

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bruhcollective.itaysonlab.jetisteam.uikit.components.IndicatorBehindScrollableTabRow
import bruhcollective.itaysonlab.jetisteam.uikit.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.uikit.components.tabIndicatorOffset
import bruhcollective.itaysonlab.ksteam.guard.models.CodeModel
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.guard.R
import bruhcollective.itaysonlab.microapp.guard.ui.variants.GuardMainScreen
import bruhcollective.itaysonlab.microapp.guard.ui.variants.NoGuardScreen
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GuardScreen(
    viewModel: GuardViewModel = hiltViewModel(),
    onAddClicked: (Long) -> Unit,
) {
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val snackState = remember { SnackbarHostState() }

    Scaffold(topBar = {
        if (viewModel.state is GuardViewModel.GuardState.Available) {
            IndicatorBehindScrollableTabRow(
                selectedTabIndex = 0,
                containerColor = Color.Transparent,
                indicator = { tabPositions ->
                    Box(
                        Modifier
                            .padding(vertical = 12.dp)
                            .tabIndicatorOffset(tabPositions[0])
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSurface)
                    )
                },
                edgePadding = 16.dp,
                modifier = Modifier.statusBarsPadding(),
                tabAlignment = Alignment.Center
            ) {
                Tab(
                    selected = true,
                    onClick = { /*TODO*/ },
                    selectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ) {
                    Text(
                        text = stringResource(id = R.string.guard).uppercase(Locale.getDefault()),
                        modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp),
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )
                }

                Tab(
                    selected = false,
                    onClick = { /*TODO*/ },
                    selectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ) {
                    Text(
                        text = stringResource(id = R.string.guard_sessions).uppercase(Locale.getDefault()),
                        modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp),
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }, contentWindowInsets = EmptyWindowInsets, snackbarHost = {
        SnackbarHost(hostState = snackState) {
            Snackbar(snackbarData = it)
        }
    }) { innerPadding ->
        when (val state = viewModel.state) {
            is GuardViewModel.GuardState.Available -> {
                val codeState = state.instance.code.collectAsStateWithLifecycle(initialValue = CodeModel.DefaultInstance)

                /*val confirmState = viewModel.confirmationFlow.collectAsStateWithLifecycle(initialValue = 0L)

                LaunchedEffect(confirmState.value) {
                    if (confirmState.value != 0L) {
                        onSessionArrived(viewModel.steamId, confirmState.value)
                    }
                }*/

                RoundedPage(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)) {
                    GuardMainScreen(
                        modifier = Modifier.fillMaxSize(),
                        code = codeState.value,
                        accountName = state.instance.username,
                        onMoreSettingsClicked = {

                        }, onSignWithQrClicked = {

                        }, onCopyClicked = {
                            scope.launch {
                                clipboard.setText(AnnotatedString(codeState.value.code))

                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                                    snackState.showSnackbar(message = context.getString(R.string.guard_actions_copy_snack))
                                }
                            }
                        }
                    )
                }
            }

            GuardViewModel.GuardState.Setup -> {
                NoGuardScreen(
                    onAddClicked = { onAddClicked(viewModel.steamId) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }
    }
}
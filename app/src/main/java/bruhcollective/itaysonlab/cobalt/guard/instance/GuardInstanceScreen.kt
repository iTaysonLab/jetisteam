package bruhcollective.itaysonlab.cobalt.guard.instance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material3.Badge
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.GuardConfirmSessionSheet
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.GuardRecoveryCodeSheet
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.GuardRemoveSheet
import bruhcollective.itaysonlab.cobalt.guard.qr_code.GuardQrCodeSheet
import bruhcollective.itaysonlab.cobalt.ui.components.EmptyWindowInsets
import bruhcollective.itaysonlab.cobalt.ui.components.IndicatorBehindScrollableTabRow
import bruhcollective.itaysonlab.cobalt.ui.components.tabIndicatorOffset
import com.arkivanov.decompose.extensions.compose.pages.ChildPages
import com.arkivanov.decompose.extensions.compose.pages.PagesScrollAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import java.util.Locale

@Composable
fun GuardInstanceScreen(
    component: GuardInstanceComponent,
) {
    val pages by component.pages.subscribeAsState()
    val alert by component.alertSlot.subscribeAsState()

    alert.child?.instance?.let {
        when (val child = it) {
            is GuardInstanceComponent.AlertChild.DeleteGuard -> {
                GuardRemoveSheet(child.component)
            }

            is GuardInstanceComponent.AlertChild.IncomingSession -> {
                GuardConfirmSessionSheet(child.component)
            }

            is GuardInstanceComponent.AlertChild.QrCodeScanner -> {
                GuardQrCodeSheet(child.component)
            }

            is GuardInstanceComponent.AlertChild.RecoveryCode -> {
                GuardRecoveryCodeSheet(child.component)
            }
        }
    }

    Scaffold(topBar = {
        GuardInstanceHeader(
            selectedIndex = pages.selectedIndex,
            onPageSelected = component::selectPage,
            modifier = Modifier.statusBarsPadding()
        )
    }, floatingActionButton = {
        FloatingActionButton(onClick = component::openQrScanner) {
            Icon(
                imageVector = Icons.Rounded.QrCodeScanner,
                contentDescription = stringResource(id = R.string.guard_qr_action)
            )
        }
    }, contentWindowInsets = EmptyWindowInsets) { innerPadding ->
        ChildPages(
            pages = pages,
            onPageSelected = component::selectPage,
            scrollAnimation = PagesScrollAnimation.Default,
            modifier = Modifier.fillMaxSize().padding(innerPadding),
        ) { _, page ->
            when (page) {
                is GuardInstanceComponent.PageChild.Code -> {
                    GuardCodePage(page.component)
                }

                is GuardInstanceComponent.PageChild.Confirmations -> {
                    GuardConfirmationsPage(page.component)
                }

                is GuardInstanceComponent.PageChild.Sessions -> {
                    GuardSessionsPage(page.component)
                }
            }
        }
    }
}

@Composable
private fun GuardInstanceHeader(
    selectedIndex: Int,
    onPageSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    confirmationCount: Int = 0
) {
    IndicatorBehindScrollableTabRow(
        selectedTabIndex = selectedIndex,
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            Box(
                Modifier
                    .padding(vertical = 12.dp)
                    .tabIndicatorOffset(tabPositions[selectedIndex])
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface)
            )
        },
        edgePadding = 16.dp,
        modifier = modifier,
        tabAlignment = Alignment.Center
    ) {
        Tab(
            selected = selectedIndex == 0,
            onClick = { onPageSelected(0) },
            selectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ) {
            Text(
                text = stringResource(id = R.string.guard_code).uppercase(Locale.getDefault()),
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp),
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
        }

        Tab(
            selected = selectedIndex == 1,
            onClick = { onPageSelected(1) },
            selectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ) {
            if (confirmationCount > 0) {
                val contentColor = LocalContentColor.current

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.guard_confirms).uppercase(Locale.getDefault()),
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )

                    Badge(
                        containerColor = LocalContentColor.current,
                        contentColor = Color(
                            red = contentColor.colorSpace.getMaxValue(0) - contentColor.red,
                            green = contentColor.colorSpace.getMaxValue(1) - contentColor.green,
                            blue = contentColor.colorSpace.getMaxValue(2) - contentColor.blue
                        )
                    ) {
                        Text(text = confirmationCount.toString())
                    }
                }
            } else {
                Text(
                    text = stringResource(id = R.string.guard_confirms).uppercase(Locale.getDefault()),
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp)
                )
            }
        }

        Tab(
            selected = selectedIndex == 2,
            onClick = { onPageSelected(2) },
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
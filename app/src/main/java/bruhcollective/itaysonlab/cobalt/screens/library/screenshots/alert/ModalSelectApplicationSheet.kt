package bruhcollective.itaysonlab.cobalt.screens.library.screenshots.alert

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.library.screenshots.SelectApplicationSheetComponent
import bruhcollective.itaysonlab.cobalt.ui.components.BottomSheetHeader
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ModalSelectApplicationSheet (
    onDismiss: () -> Unit,
    component: SelectApplicationSheetComponent
) {
    val isLoading by component.isLoading.subscribeAsState()
    val applications by component.applications.subscribeAsState()

    ModalBottomSheet(onDismissRequest = onDismiss) {
        BottomSheetHeader(
            text = stringResource(R.string.library_screenshots_chip_apps),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isLoading) {
            LoadingIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn {
                item {
                    Column {
                        ListItem(
                            headlineContent = {
                                Text(stringResource(R.string.library_games_chip_collections_default))
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth().clickable(onClick = component::onClearFilterClicked)
                        )

                        HorizontalDivider()
                    }
                }

                items(applications) { app ->
                    Column {
                        ListItem(
                            headlineContent = {
                                Text(app.name)
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth().clickable {
                                component.onApplicationClicked(app)
                            }
                        )

                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
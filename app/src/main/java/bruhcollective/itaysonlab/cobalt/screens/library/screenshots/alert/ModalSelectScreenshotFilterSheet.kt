package bruhcollective.itaysonlab.cobalt.screens.library.screenshots.alert

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.library.screenshots.ScreenshotsFilterSheetComponent
import bruhcollective.itaysonlab.cobalt.ui.components.BottomSheetHeader
import bruhcollective.itaysonlab.ksteam.handlers.PublishedFiles
import com.arkivanov.decompose.extensions.compose.subscribeAsState

private val sortOrders: List<Pair<PublishedFiles.PersonalSortOrder, Int>> = listOf(
    PublishedFiles.PersonalSortOrder.MostPopular to R.string.library_screenshots_filter_sort_popular,
    PublishedFiles.PersonalSortOrder.NewestFirst to R.string.library_screenshots_filter_sort_newest,
    PublishedFiles.PersonalSortOrder.OldestFirst to R.string.library_screenshots_filter_sort_oldest,
)

private val privacyFilters: List<Pair<PublishedFiles.PersonalPrivacyFilter, Int>> = listOf(
    PublishedFiles.PersonalPrivacyFilter.Everything to R.string.library_screenshots_filter_privacy_everything,
    PublishedFiles.PersonalPrivacyFilter.Public to R.string.library_screenshots_filter_privacy_public,
    PublishedFiles.PersonalPrivacyFilter.FriendsOnly to R.string.library_screenshots_filter_privacy_friends,
    PublishedFiles.PersonalPrivacyFilter.Private to R.string.library_screenshots_filter_privacy_private,
    PublishedFiles.PersonalPrivacyFilter.LinkOnly to R.string.library_screenshots_filter_privacy_link,
)

private val browseFilters: List<Pair<PublishedFiles.PersonalBrowseFilter, Int>> = listOf(
    PublishedFiles.PersonalBrowseFilter.Created to R.string.library_screenshots_filter_created,
    PublishedFiles.PersonalBrowseFilter.Favorites to R.string.library_screenshots_filter_favorites,
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ModalSelectScreenshotFilterSheet(
    onDismiss: () -> Unit,
    component: ScreenshotsFilterSheetComponent
) {
    val sortOrder by component.sortOrder.subscribeAsState()
    val privacyFilter by component.privacyFilter.subscribeAsState()
    val browseFilter by component.browseFilter.subscribeAsState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        BottomSheetHeader(
            text = stringResource(R.string.library_screenshots_filter_sheet),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f, fill = false),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            category {
                stringResource(R.string.library_screenshots_filter_sort)
            }

            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    for ((def, titleResId) in sortOrders) {
                        CheckFilterChip(
                            selected = sortOrder == def,
                            onClick = { component.setSortOrder(def) },
                            label = { Text(stringResource(titleResId)) },
                        )
                    }
                }
            }

            divider()

            category {
                stringResource(R.string.library_screenshots_filter_privacy)
            }

            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    for ((def, titleResId) in privacyFilters) {
                        CheckFilterChip(
                            selected = privacyFilter == def,
                            onClick = { component.setPrivacyFilter(def) },
                            label = { Text(stringResource(titleResId)) },
                        )
                    }
                }
            }

            divider()

            category {
                stringResource(R.string.library_screenshots_filter_browse)
            }

            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    for ((def, titleResId) in browseFilters) {
                        CheckFilterChip(
                            selected = browseFilter == def,
                            onClick = { component.setBrowseFilter(def) },
                            label = { Text(stringResource(titleResId)) },
                        )
                    }
                }
            }
        }

        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilledTonalButton(
                onClick = onDismiss,
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(text = stringResource(R.string.library_screenshots_filter_action_cancel))
            }

            Button(
                onClick = component::applyChanges,
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(text = stringResource(R.string.library_screenshots_filter_action_apply))
            }
        }
    }
}

@Composable
private fun CheckFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = label,
        leadingIcon = if (selected) {
            { Icon(Icons.Rounded.Check, contentDescription = null) }
        } else null
    )
}

private fun LazyListScope.category(text: @Composable () -> String) {
    item {
        Text(
            text = text(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun LazyListScope.divider() {
    item {
        HorizontalDivider(modifier = Modifier.padding(bottom = 4.dp))
    }
}
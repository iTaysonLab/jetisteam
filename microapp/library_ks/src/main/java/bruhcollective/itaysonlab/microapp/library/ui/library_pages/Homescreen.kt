package bruhcollective.itaysonlab.microapp.library.ui.library_pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Badge
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.ksteam.handlers.library
import bruhcollective.itaysonlab.ksteam.models.library.LibraryShelf
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Composable
internal fun Homescreen(
    viewModel: HomescreenViewModel = hiltViewModel()
) {
    val shelves = viewModel.homeScreenShelves.collectAsStateWithLifecycle()

    LazyColumn(Modifier.fillMaxSize()) {
        items(shelves.value) { shelf ->
            Shelf(shelf)
        }
    }
}

@HiltViewModel
internal class HomescreenViewModel @Inject constructor(
    private val steamClient: HostSteamClient
): ViewModel() {
    val homeScreenShelves get() = steamClient.client.library.shelves
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Shelf(
    shelf: LibraryShelf
) {
    Column(Modifier.fillMaxWidth()) {

        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = shelf.id, style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.width(8.dp))

            Badge {
                Text(text = shelf.linkedCollection)
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(6) {
                LibraryItem(
                    "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .width(140.dp),
                    placeholderColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Divider(color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExamplePlayNextShelf() {
    Column(Modifier.fillMaxWidth()) {

        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(text = "Play Next", style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = "Players like you love these unplayed games in your library",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(6) {
                LibraryItem(
                    "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .width(240.dp),
                    placeholderColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Divider(color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExampleCollectionsShelf() {
    Column(Modifier.fillMaxWidth()) {

        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Collections View", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.width(8.dp))

            Badge {
                Text(text = "8")
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(6) {
                Box(
                    Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
                        .size(140.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Divider(color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
    }
}
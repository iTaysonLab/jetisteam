package bruhcollective.itaysonlab.microapp.gamepage.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.jetisteam.models.Review

@Composable
fun GamePageReview(
    review: Review,
    modifier: Modifier
) {
    Card(modifier) {
        Column(Modifier.padding(16.dp).fillMaxWidth()) {
            Text(text = review.review)
        }
    }
}
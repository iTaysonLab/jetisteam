package bruhcollective.itaysonlab.microapp.gamepage.ui.components

import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import bruhcollective.itaysonlab.jetisteam.models.Review

@Composable
fun GamePageReview(
    review: Review,
    modifier: Modifier
) {
    Card(modifier) {
        Text(text = review.review)
    }
}
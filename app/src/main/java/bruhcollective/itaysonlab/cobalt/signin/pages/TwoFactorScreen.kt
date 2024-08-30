package bruhcollective.itaysonlab.cobalt.signin.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material.icons.sharp.Check
import androidx.compose.material.icons.sharp.SecurityUpdateGood
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.signin.tfa.TwoFactorComponent
import bruhcollective.itaysonlab.cobalt.ui.components.CobaltDivider
import bruhcollective.itaysonlab.cobalt.ui.components.CobaltTextField
import bruhcollective.itaysonlab.cobalt.ui.components.InlineMonoButton
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@OptIn(ExperimentalTextApi::class)
@Composable
fun TwoFactorScreen(
    component: TwoFactorComponent
) {
    val code by component.code.subscribeAsState()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .imePadding()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .verticalScroll(rememberScrollState())
        ) {
            IconButton(
                onClick = component::onBackClicked,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Sharp.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Two-factor authorization",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "To sign in, enter the Steam Guard code.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            CobaltDivider()

            ListItem(headlineContent = {
                Text(text = "You can also confirm the authorization in any supported Steam authenticator.")
            }, leadingContent = {
                Icon(imageVector = Icons.Sharp.SecurityUpdateGood, contentDescription = null)
            }, colors = ListItemDefaults.colors(containerColor = Color.Transparent), modifier = Modifier.padding(vertical = 8.dp))

            CobaltDivider()

            CobaltTextField(
                value = code,
                onValueChange = component::onCodeChanged,
                label = "Code",
                placeholder = "1A2BC",
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .focusRequester(focusRequester)
            )

            CobaltDivider()

            InlineMonoButton(
                icon = {
                    Icon(
                        imageVector = Icons.Sharp.Check,
                        contentDescription = null
                    )
                },
                title = "Submit code",
                onClick = component::onBackClicked
            )
        }
    }
}
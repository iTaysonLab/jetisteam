package bruhcollective.itaysonlab.microapp.guard.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeRow(
    state: CodeRowState
) {
    val focusManager = LocalFocusManager.current

    Row(horizontalArrangement = Arrangement.Center) {
        state.entries.forEachIndexed { index, entry ->
            OutlinedTextField(
                value = entry.number,
                onValueChange = {
                    if (entry.number.text == it.text) return@OutlinedTextField

                    if (it.text.length <= 1) {
                        entry.number = it
                    }

                    if (it.text.isNotEmpty()) {
                        if (index == state.entries.lastIndex) {
                            focusManager.clearFocus()
                            state.finish()
                        } else {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .width(48.dp)
                    .focusTarget()
                    .focusRequester(entry.focus)
                    .focusProperties {
                        state.entries.getOrNull(index + 1)?.focus?.let { next = it }
                    },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                        8.dp
                    ), unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Number
                )
            )
        }
    }
}

class CodeRowState(
    count: Int,
    private val onFinish: (String) -> Unit
) {
    val entries = (1..count).map { CodeEntry() }

    fun finish() {
        onFinish(entries.joinToString(separator = "") { e -> e.number.text })
    }

    class CodeEntry {
        var number by mutableStateOf(TextFieldValue(""))
        val focus = FocusRequester()
    }
}
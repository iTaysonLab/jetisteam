package bruhcollective.itaysonlab.jetisteam.guard.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.guard.setup.sms.CodeRowComponent
import bruhcollective.itaysonlab.jetisteam.ui.font.robotoMonoFontFamily
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.delay

// TODO: "Delete" should focus on previous field
@Composable
fun CodeRow(
    component: CodeRowComponent
) {
    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current

    val inactive by component.inactive.subscribeAsState()
    val error by component.error.subscribeAsState()

    val focusArray = remember (component.codeLength) {
        Log.d("TAG", "CodeRow: focusArray init")
        Array(component.codeLength) { FocusRequester() }
    }

    LaunchedEffect(error) {
        if (error) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            delay(1500L)
            component.setError(false)
        }
    }

    Row(horizontalArrangement = Arrangement.Center) {
        repeat(component.codeLength) { index ->
            val number by component.getEntryCode(index).subscribeAsState()

            OutlinedTextField(
                value = number,
                onValueChange = {
                    if (number == it) return@OutlinedTextField

                    if (it.length <= 1) {
                        component.setEntryCode(index, it)
                    }

                    if (it.isNotEmpty()) {
                        if (index == (component.codeLength - 1)) {
                            focusManager.clearFocus()
                            component.onEntryFinish()
                        } else {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .width(48.dp)
                    .focusTarget()
                    .focusRequester(focusArray[index])
                    .focusProperties {
                        focusArray.getOrNull(index + 1)?.let { next = it }
                    },
                enabled = inactive.not(),
                isError = error,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp),
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp),
                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, fontFamily = robotoMonoFontFamily),
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Number
                )
            )
        }
    }
}
package com.jesushz.spendless.auth.presentation.pin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jesushz.spendless.R
import com.jesushz.spendless.auth.domain.PinFlow
import com.jesushz.spendless.auth.presentation.pin.components.NumericKeyboard
import com.jesushz.spendless.auth.presentation.pin.components.OtpInput
import com.jesushz.spendless.core.presentation.designsystem.components.SpendLessScaffold
import com.jesushz.spendless.core.presentation.designsystem.components.SpendLessTopBar
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme
import com.jesushz.spendless.core.presentation.ui.ObserveAsEvents
import kotlinx.coroutines.launch

@Composable
fun PinScreenRoot(
    viewModel: PinViewModel = viewModel(),
    onNavigateUp: () -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    ObserveAsEvents(
        flow = viewModel.event
    ) { event ->
        when (event) {
            is PinEvent.OnError -> {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = event.message.asString(context)
                    )
                }
            }
        }
    }
    PinScreen(
        state = state,
        snackBarHostState = snackBarHostState,
        onAction = { action ->
            when (action) {
                PinAction.OnBackPressed -> onNavigateUp()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun PinScreen(
    state: PinState,
    snackBarHostState: SnackbarHostState,
    onAction: (PinAction) -> Unit,
) {
    SpendLessScaffold(
        topBar = {
            SpendLessTopBar(
                onNavigateBack = {
                    onAction(PinAction.OnBackPressed)
                }
            )
        },
        snackBarHost = snackBarHostState
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Header(
                flow = state.flow
            )
            OtpInput(
                pin = if (state.flow == PinFlow.CONFIRM) state.confirmPin else state.pin,
                maxDigits = PinViewModel.PIN_LENGTH,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            NumericKeyboard(
                modifier = Modifier
                    .weight(1f),
                onKeyClick = {
                    onAction(PinAction.OnKeyPressed(it))
                }
            )
        }
    }
}

@Composable
private fun Header(
    flow: PinFlow
) {
    Text(
        text = when (flow) {
            PinFlow.REGISTER -> stringResource(R.string.create_pin)
            PinFlow.LOGIN -> stringResource(R.string.create_pin)
            PinFlow.CONFIRM -> stringResource(R.string.repeat_pin)
            PinFlow.REFRESH_LOGIN -> stringResource(R.string.create_pin)
        },
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = when (flow) {
            PinFlow.REGISTER -> stringResource(R.string.create_pin_description)
            PinFlow.LOGIN -> stringResource(R.string.create_pin_description)
            PinFlow.CONFIRM -> stringResource(R.string.repeat_pin_description)
            PinFlow.REFRESH_LOGIN -> stringResource(R.string.create_pin_description)
        },
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Spacer(modifier = Modifier.height(36.dp))
}

@Preview
@Composable
private fun PinScreenPreview() {
    SpendLessTheme {
        PinScreen(
            state = PinState(),
            snackBarHostState = SnackbarHostState(),
            onAction = {}
        )
    }
}

@file:OptIn(ExperimentalComposeUiApi::class)

package com.jesushz.spendless.auth.presentation.login

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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.platform.LocalAutofillManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jesushz.spendless.R
import com.jesushz.spendless.auth.presentation.login.components.PinLoginTextField
import com.jesushz.spendless.auth.presentation.login.components.UsernameLoginTextField
import com.jesushz.spendless.core.presentation.designsystem.components.SpendLessButton
import com.jesushz.spendless.core.presentation.designsystem.components.SpendLessScaffold
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme
import com.jesushz.spendless.core.presentation.ui.ObserveAsEvents
import kotlinx.coroutines.launch

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel,
    onNavigateToRegister: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    onRequestBiometrics: () -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    ObserveAsEvents(
        flow = viewModel.event,
    ) { event ->
        when (event) {
            is LoginEvent.OnError -> {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = event.error.asString(context)
                    )
                }
            }
            LoginEvent.OnLoginSuccess -> onNavigateToDashboard()
            LoginEvent.OnRequestBiometrics -> onRequestBiometrics()
        }
    }
    LoginScreen(
        state = state,
        snackBarHostState = snackBarHostState,
        onAction = { action ->
            when (action) {
                LoginAction.OnRegisterClick -> {
                    onNavigateToRegister()
                }
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    snackBarHostState: SnackbarHostState,
    onAction: (LoginAction) -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val autofillManager = LocalAutofillManager.current

    SpendLessScaffold(
        snackBarHost = snackBarHostState
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.welcome_back),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.enter_your_credentials),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(36.dp))
            UsernameLoginTextField(
                value = state.username,
                hint = stringResource(R.string.username_hint_login),
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentType = ContentType.Username
                    },
                onValueChange = {
                    onAction(LoginAction.OnUsernameChange(it))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            PinLoginTextField(
                value = state.pin,
                hint = stringResource(R.string.pin_hint),
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentType = ContentType.Password
                    },
                onValueChange = {
                    onAction(LoginAction.OnPinChange(it))
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            SpendLessButton(
                onButtonClick = {
                    keyboard?.hide()
                    autofillManager?.commit()
                        .also {
                            onAction(LoginAction.OnLoginClick)
                        }
                },
                isEnabled = state.canLogin,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.login),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.height(28.dp))
            TextButton(
                onClick = {
                    onAction(LoginAction.OnRegisterClick)
                }
            ) {
                Text(
                    text = stringResource(R.string.new_to_spend_less),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    SpendLessTheme {
        LoginScreen(
            state = LoginState(),
            snackBarHostState = SnackbarHostState(),
            onAction = {}
        )
    }
}
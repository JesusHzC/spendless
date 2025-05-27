package com.jesushz.spendless.auth.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jesushz.spendless.R
import com.jesushz.spendless.auth.presentation.register.components.UsernameRegisterTextField
import com.jesushz.spendless.core.presentation.designsystem.components.SpendLessButton
import com.jesushz.spendless.core.presentation.designsystem.components.SpendLessScaffold
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme
import com.jesushz.spendless.core.presentation.ui.ObserveAsEvents
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreenRoot(
    viewModel: RegisterViewModel = koinViewModel(),
    onNavigateToPin: (username: String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    ObserveAsEvents(
        flow = viewModel.event,
    ) { event ->
        when (event) {
            is RegisterEvent.OnError -> {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = event.error.asString(context)
                    )
                }
            }
            is RegisterEvent.OnUsernameSuccess -> {
                onNavigateToPin(event.username)
            }
        }
    }
    RegisterScreen(
        state = state,
        snackBarHostState = snackBarHostState,
        onAction = { action ->
            when (action) {
                RegisterAction.OnLoginClick -> {
                    onNavigateToLogin()
                }
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun RegisterScreen(
    state: RegisterState,
    snackBarHostState: SnackbarHostState,
    onAction: (RegisterAction) -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current
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
                text = stringResource(R.string.welcome),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.create_unique_username),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(36.dp))
            UsernameRegisterTextField(
                value = state.username,
                hint = stringResource(R.string.username_hint),
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    onAction(RegisterAction.OnUsernameChange(it))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SpendLessButton(
                isEnabled = state.isUsernameValid,
                modifier = Modifier
                    .fillMaxWidth(),
                onButtonClick = {
                    keyboard?.hide()
                    onAction(RegisterAction.OnNextButtonClick)
                },
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.next),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(28.dp))
            TextButton(
                onClick = {
                    onAction(RegisterAction.OnLoginClick)
                }
            ) {
                Text(
                    text = stringResource(R.string.already_have_an_account),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    SpendLessTheme {
        RegisterScreen(
            state = RegisterState(),
            snackBarHostState = SnackbarHostState(),
            onAction = {}
        )
    }
}

package com.jesushz.spendless.auth.presentation.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme

@Composable
fun RegisterScreenRoot() {

}

@Composable
private fun RegisterScreen(
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {  }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    SpendLessTheme {
        RegisterScreen()
    }
}

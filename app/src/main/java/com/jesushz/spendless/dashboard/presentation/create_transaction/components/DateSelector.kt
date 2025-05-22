@file:OptIn(ExperimentalMaterial3Api::class)

package com.jesushz.spendless.dashboard.presentation.create_transaction.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jesushz.spendless.R
import com.jesushz.spendless.core.util.formatToReadableDate
import com.jesushz.spendless.core.util.parseDateToMillis
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun DateSelector(
    modifier: Modifier = Modifier,
    dateSelected: String,
    onDateSelected: (String) -> Unit
) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }

    LaunchedEffect(selectedDate) {
        selectedDate?.let {
            onDateSelected(convertMillisToDate(it))
        }
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(16.dp),
        onClick = {
           showModal = true
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.date),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = formatToReadableDate(dateSelected),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }

    if (showModal) {
        DatePickerModal(
            dateSelected = parseDateToMillis(dateSelected),
            onDateSelected = { selectedDate = it },
            onDismiss = { showModal = false }
        )
    }
}

@Composable
fun DatePickerModal(
    dateSelected: Long,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = dateSelected
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    text = stringResource(R.string.pick_date),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(24.dp)
                )
            }
        )
    }
}

private fun convertMillisToDate(millis: Long): String {
    val date = Instant.ofEpochMilli(millis)
        .atZone(ZoneOffset.UTC)
        .toLocalDateTime()

    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    return date.format(formatter)
}

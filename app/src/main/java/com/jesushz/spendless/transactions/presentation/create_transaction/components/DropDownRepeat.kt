package com.jesushz.spendless.transactions.presentation.create_transaction.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.jesushz.spendless.core.domain.transactions.Repeat
import com.jesushz.spendless.core.presentation.designsystem.theme.PrimaryFixed
import com.jesushz.spendless.core.util.userInteraction

@Composable
fun DropDownRepeat(
    modifier: Modifier = Modifier,
    repeatSelected: Repeat,
    onRepeatTypeSelected: (Repeat) -> Unit
) {
    var menuMaxWidth by remember {
        mutableIntStateOf(0)
    }
    val menuMaxWidthDp = with(LocalDensity.current) { menuMaxWidth.toDp() }

    var menuIsExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .onSizeChanged { size ->
                menuMaxWidth = maxOf(menuMaxWidth, size.width)
            }
    ) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(16.dp),
            onClick = {
                menuIsExpanded = true
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .size(40.dp)
                        .background(
                            color = PrimaryFixed
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "\uD83D\uDD04"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = repeatSelected.title,
                    modifier = Modifier
                        .weight(1f),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        }
        DropdownMenu(
            expanded = menuIsExpanded,
            onDismissRequest = { menuIsExpanded = false },
            containerColor = MaterialTheme.colorScheme.onPrimary,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .width(menuMaxWidthDp)
                .userInteraction()
        ) {
            Repeat.entries.fastForEach { type ->
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.weight(0.1f))
                            Text(
                                text = type.title,
                                modifier = Modifier
                                    .weight(1f),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    },
                    onClick = {
                        menuIsExpanded = false
                        onRepeatTypeSelected(type)
                    }
                )
            }
        }
    }
}

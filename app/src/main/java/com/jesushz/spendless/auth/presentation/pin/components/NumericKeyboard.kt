@file:OptIn(ExperimentalLayoutApi::class)

package com.jesushz.spendless.auth.presentation.pin.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jesushz.spendless.R
import com.jesushz.spendless.core.presentation.designsystem.theme.DeleteKeyColor
import com.jesushz.spendless.core.presentation.designsystem.theme.OnPrimaryFixed
import com.jesushz.spendless.core.presentation.designsystem.theme.PrimaryFixed

enum class Keys(val value: Int) {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    BLANK(-2),
    ZERO(0),
    DELETE(-1);
}

@Composable
fun NumericKeyboard(
    modifier: Modifier = Modifier,
    onKeyClick: (Keys) -> Unit
) {
    var keyMaxWidth by remember {
        mutableIntStateOf(0)
    }
    val keyMaxWidthDp = with(LocalDensity.current) { keyMaxWidth.toDp() - 8.dp }
    var keyMaxHeight by remember {
        mutableIntStateOf(0)
    }
    val keyMaxHeightDp = with(LocalDensity.current) { keyMaxHeight.toDp() - 8.dp }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        userScrollEnabled = false,
        modifier = modifier
            .onSizeChanged { size ->
                val width = size.width / 3
                val height = size.height / 4
                keyMaxWidth = maxOf(keyMaxWidth, width)
                keyMaxHeight = maxOf(keyMaxHeight, height)
            }
    ) {
        items(
            items = Keys.entries.toList(),
            key = { it.ordinal }
        ) { key ->
            Box(
                modifier = Modifier
                    .width(keyMaxWidthDp)
                    .height(keyMaxHeightDp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(
                        color = when (key) {
                            Keys.DELETE -> DeleteKeyColor
                            Keys.BLANK -> Color.Transparent
                            else -> PrimaryFixed
                        }
                    )
                    .clickable(enabled = key != Keys.BLANK) {
                        onKeyClick(key)
                    },
                contentAlignment = Alignment.Center
            ) {
                when (key) {
                    Keys.BLANK -> Unit
                    Keys.DELETE -> Image(
                        painter = painterResource(R.drawable.ic_back_key),
                        contentDescription = stringResource(R.string.delete_key),
                    )
                    else -> Text(
                        text = key.value.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = OnPrimaryFixed
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun NumericKeyboardPreview() {
    NumericKeyboard(
        modifier = Modifier
            .fillMaxWidth(),
        onKeyClick = {}
    )
}

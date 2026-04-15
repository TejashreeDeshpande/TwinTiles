package com.example.twintiles.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

sealed class CardState {
    data class Visible(val value: Int) : CardState()
    data class Matched(val value: Int) : CardState()
}

fun generateShuffledPairs(): List<Int> {
    val list = (1..6).flatMap { listOf(it, it) } // [1,1,2,2,...,6,6]
    return list.shuffled()
}

@Composable
fun GridScreen(modifier: Modifier) {

    var items by remember {
        mutableStateOf<List<CardState>>(generateShuffledPairs().map {
            CardState.Visible(it)
        })
    }

    var selected1 by remember { mutableStateOf<Int?>(null) }
    var selected2 by remember { mutableStateOf<Int?>(null) }

    Column {
        Box(
            modifier = modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    items = generateShuffledPairs().map { CardState.Visible(it) }
                    selected1 = null
                    selected2 = null
                },
                modifier = modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = Color.Red
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = modifier
                .fillMaxWidth()
                .padding(1.dp),
            contentPadding = PaddingValues(4.dp)
        ) {
            itemsIndexed(items) { index, state ->
                GridCard(state) {

                    if (selected1 == null) {
                        selected1 = index
                    } else {
                        selected2 = index

                        val first = selected1!!
                        val second = selected2!!

                        // prevent same card click
                        if (first != second) {

                            val isMatch =
                                (items[first] as CardState.Visible).value ==
                                        (items[second] as CardState.Visible).value

                            if (isMatch) {

                                items = items.mapIndexed { i, state ->
                                    when (state) {
                                        is CardState.Visible -> {
                                            if (i == first || i == second) {
                                                CardState.Matched(state.value)
                                            } else state
                                        }

                                        is CardState.Matched -> state
                                    }
                                }
                            }
                        }

                        // reset selection always
                        selected1 = null
                        selected2 = null
                    }
                }
            }
        }
    }
}

@Composable
fun GridCard(
    state: CardState,
    onClick: () -> Unit
) {

    val (color, text) = when (state) {
        is CardState.Visible -> MaterialTheme.colorScheme.surface to "Card ${state.value}"
        is CardState.Matched -> Color.Green to state.value.toString()
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(1f)
            .clickable {
                if (state is CardState.Visible) {
                    onClick()
                }
            },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = text)
        }
    }
}
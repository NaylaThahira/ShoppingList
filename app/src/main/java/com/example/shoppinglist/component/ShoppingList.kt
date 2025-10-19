package com.example.shoppinglist.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shoppinglist.ui.theme.ShoppingListTheme
import com.example.shoppinglist.screen.Screen

// KOMPONEN WRAPPER UNTUK MENGELOLA STATE SELEKSI
@Composable
fun ShoppingList(items: List<String>, onDeleteItem: (String) -> Unit, navController: NavController) {

    // State yang melacak item mana yang sedang dipilih/diklik
    var selectedItem by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = items,
            key = { it } // Tetap gunakan key untuk performa
        ) { item ->
            ItemCardWithActions(
                itemName = item,
                // Mengirim status seleksi
                isSelected = selectedItem == item,
                onItemClick = {
                    // ❗ LOGIKA UTAMA: Klik Card hanya me-toggle seleksi
                    selectedItem = if (selectedItem == item) null else item
                },
                onDelete = {
                    onDeleteItem(item)
                    selectedItem = null // Hapus seleksi setelah terhapus
                },
                onNavigateToDetail = {
                    navController.navigate("detail/$item")
                    selectedItem = null // Hapus seleksi sebelum navigasi
                }
            )
        }
    }
}

// KOMPONEN CARD DENGAN LOGIKA TOMBOL DETAIL/HAPUS
@Composable
fun ItemCardWithActions(
    itemName: String,
    isSelected: Boolean,
    onItemClick: (String) -> Unit,
    onDelete: () -> Unit,
    onNavigateToDetail: () -> Unit
) {
    // Animasi Hover/Press
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val itemContainerColor by animateColorAsState(
        targetValue = when {
            isSelected -> MaterialTheme.colorScheme.primaryContainer // Klik/Selected
            isPressed -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f) // Hover/Tekan
            else -> MaterialTheme.colorScheme.surface // Default
        },
        animationSpec = tween(200),
        label = "itemContainerColor"
    )

    val itemTextColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        animationSpec = tween(200),
        label = "itemTextColor"
    )

    val itemElevation by animateDpAsState(
        targetValue = if (isPressed || isSelected) 8.dp else 4.dp,
        animationSpec = tween(200),
        label = "itemElevation"
    )

    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .shadow(itemElevation, RoundedCornerShape(12.dp), clip = false)
                .clickable (
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = { onItemClick(itemName) } // KLIK CARD HANYA MENGUBAH STATE SELEKSI
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = itemContainerColor,
                contentColor = itemTextColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            border = BorderStroke(
                width = 1.dp,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                } else {
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
                }
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon status
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Status",
                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Nama item
                Text(
                    text = itemName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        textDecoration = null
                    ),
                    color = itemTextColor,
                    modifier = Modifier.weight(1f)
                )

                // Placeholder agar item terisi penuh
                Spacer(modifier = Modifier.size(24.dp))
            }
        }

        // ANIMATED VISIBILITY: Tombol Detail dan Hapus
        AnimatedVisibility(
            visible = isSelected,
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
            exit = fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onNavigateToDetail, // KLIK NAVIGASI DI TOMBOL
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.Info, contentDescription = "Detail")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Item Details")
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = onDelete, // KLIK HAPUS DI TOMBOL
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Hapus")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Clear")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemCardWithActionsPreview() {
    ShoppingListTheme {
        Column(Modifier.padding(16.dp)) {
            ItemCardWithActions(
                itemName = "Apel Fuji",
                isSelected = false,
                onItemClick = {},
                onDelete = { /* Do nothing */ },
                onNavigateToDetail = { /* Do nothing */ }
            )
            Spacer(Modifier.height(16.dp))
            ItemCardWithActions(
                itemName = "Pisang Cavendish",
                isSelected = true,
                onItemClick = {},
                onDelete = { /* Do nothing */ },
                onNavigateToDetail = { /* Do nothing */ }
            )
        }
    }
}
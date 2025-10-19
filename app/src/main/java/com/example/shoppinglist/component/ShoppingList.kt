package com.example.shoppinglist.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.ui.theme.ShoppingListTheme


@Composable
fun ShoppingListItem(
    item: String,
    onDelete: () -> Unit,
    onNavigateToDetail: () -> Unit,

) {
   var isSelected by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
   val isPressed by interactionSource.collectIsPressedAsState()

    // 2. Animasi Warna Latar Belakang (containerColor)
    val itemContainerColor by animateColorAsState(
        targetValue = when {
            isSelected -> MaterialTheme.colorScheme.primaryContainer // Selesai
            isPressed -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f) // Hover/Tekan
            else -> MaterialTheme.colorScheme.surface // Default
        },
        animationSpec = tween(200),
        label = "itemContainerColor"
    )

    // 3. Animasi Warna Teks (contentColor)
    val itemTextColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        animationSpec = tween(200),
        label = "itemTextColor"
    )

    // 4. Animasi Elevasi/Bayangan
    val itemElevation by animateDpAsState(
        targetValue = if (isPressed) 8.dp else 4.dp,
        animationSpec = tween(200),
        label = "itemElevation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable (
                interactionSource = interactionSource,
                indication = null, // Hapus efek riak bawaan untuk animasi kustom
                onClick = {
                        onNavigateToDetail()

                }
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = itemContainerColor,
            contentColor = itemTextColor // Teks dan ikon akan menggunakan warna ini secara default
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = itemElevation),
        border = androidx.compose.foundation.BorderStroke(
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
                text = item,
                style = MaterialTheme.typography.titleMedium.copy(
                    // Tambahkan coretan jika item selesai
                    textDecoration = if (isSelected) TextDecoration.LineThrough else null
                ),
                color = itemTextColor, // Gunakan warna teks yang dianimasikan
                modifier = Modifier.weight(1f)
            )

            // Tombol Delete
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// Tambahkan preview untuk memastikan item terlihat benar
@Preview(showBackground = true)
@Composable
fun ShoppingListItemPreview() {
    ShoppingListTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(Modifier.padding(16.dp)) {
                ShoppingListItem(
                    item = "Apel Fuji",
                    onDelete = { /* Do nothing */ },
                    onNavigateToDetail = { /* Do nothing */ }
                )
                ShoppingListItem(
                    item = "Pisang Cavendish",
                    onDelete = { /* Do nothing */ },
                    onNavigateToDetail = { /* Do nothing */ }
                )
            }
        }
    }
}
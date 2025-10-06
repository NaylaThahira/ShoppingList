package com.example.shoppinglist.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SettingOption(val icon: androidx.compose.ui.graphics.vector.ImageVector, val title: String)

@Composable
fun SettingScreen() {
    val options = listOf(
        SettingOption(Icons.Default.Person, "Profile"),
        SettingOption(Icons.Default.Notifications, "Notifications"),
        SettingOption(Icons.Default.Visibility, "Appearance"),
        SettingOption(Icons.Default.Lock, "Privacy & Security"),
        SettingOption(Icons.Default.Headphones, "Help and Support"),
        SettingOption(Icons.Default.Info, "About")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(options) { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = option.icon,
                    contentDescription = option.title,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = option.title,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Next",
                    modifier = Modifier.size(24.dp)
                )
            }
            Divider(thickness = 0.5.dp)
        }
    }
}

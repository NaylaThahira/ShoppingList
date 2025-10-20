// MainActivity.kt
package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.component.ItemInput
import com.example.shoppinglist.component.SearchInput
import com.example.shoppinglist.ui.theme.ShoppingListTheme
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shoppinglist.screen.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.example.shoppinglist.component.ShoppingList

data class ShoppingItem(
    val name: String,
    val quantity: Int
)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Menu",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Setting.route)
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        val currentRoute =
                            navController.currentBackStackEntryAsState().value?.destination?.route
                        Text(
                            when (currentRoute) {
                                Screen.Profile.route -> "Profile"
                                Screen.Setting.route -> "Settings"
                                else -> "Shopping List"
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )

            },
            bottomBar = {
                BottomNavigationBar(navController)
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                AnimatedNavHost(
                    navController = navController,
                    startDestination = Screen.Home.route,
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { 1000 },
                            animationSpec = tween(500)
                        ) + fadeIn()
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { -1000 },
                            animationSpec = tween(500)
                        ) + fadeOut()
                    },
                    popEnterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { -1000 },
                            animationSpec = tween(500)
                        ) + fadeIn()
                    },
                    popExitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { 1000 },
                            animationSpec = tween(500)
                        ) + fadeOut()
                    }
                )
                {
                    composable(Screen.Home.route) { ShoppingListApp(navController) }
                    composable(Screen.Profile.route) { ProfileScreen() }
                    composable(Screen.Setting.route) { SettingScreen(navController) }
                    composable(Screen.About.route) { AboutScreen() }
                    composable(Screen.Detail.route) { backStackEntry ->
                        val itemName = backStackEntry.arguments?.getString("itemName")
                        val itemQuantity = backStackEntry.arguments?.getString("itemQuantity")?.toIntOrNull()
                        DetailScreen(itemName, itemQuantity)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController:NavHostController) {
    val items = listOf(Screen.Home, Screen.Profile)
    val icons = listOf(Icons.Default.Home, Icons.Default.Person)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEachIndexed { index, screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Home.route)
                        launchSingleTop = true
                    }
                },
                icon = { Icon(icons[index], contentDescription = screen.title) },
                label = { Text(screen.title) },

            )
        }
    }
}




@Composable
fun ShoppingListApp(navController: NavHostController) {

    var newItemText by rememberSaveable { mutableStateOf("") }
    var newQuantityText by rememberSaveable { mutableStateOf("1") }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val shoppingItems = remember { mutableStateListOf<ShoppingItem>() }
    val filteredItems by remember(searchQuery, shoppingItems) {
        derivedStateOf {
            if (searchQuery.isBlank()) {
                shoppingItems.toList() // Return a stable copy for the UI
            } else {
                shoppingItems.filter { it.name.contains(searchQuery, ignoreCase = true) }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            // Accommodate for system bars like status bar and navigation bar
            .padding(WindowInsets.safeDrawing.asPaddingValues())
            .padding(horizontal = 16.dp)
    ) {

        Text(
            text = "Welcome!",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        ItemInput(
            text = newItemText,
            onTextChange = { newItemText = it },
            quantity = newQuantityText,
            onQuantityChange = { newQuantityText = it },
            onAddItem = {
                val quantityValue = newQuantityText.toIntOrNull() ?: 1
                if (newItemText.isNotBlank()) {
                    shoppingItems.add(ShoppingItem(newItemText, quantityValue))
                    newItemText = "" // Clear the input field after adding
                    newQuantityText = "1" // Reset quantity
                }
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Shopping List",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        SearchInput(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ShoppingList(
            items = filteredItems,
            onDeleteItem = { item: ShoppingItem -> shoppingItems.remove(item) },
            navController = navController,
        )
    }
}




@Preview(showBackground = true)
@Composable
fun ShoppingListAppPreview() {
    ShoppingListTheme {
        ShoppingListApp(rememberNavController())
    }
}
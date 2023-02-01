package com.firmansyah.malangcamp.screen.pegawai

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.firmansyah.malangcamp.HomeActivity
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.screen.BotNavItemPegawai
import com.firmansyah.malangcamp.screen.NavigationGraph
import com.firmansyah.malangcamp.theme.MalangCampTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope

//  Halaman home Pegawai untuk menampung fragment
class PegawaiHomeActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        setContent {
            MalangCampTheme {
                PegawaiHome()
            }
        }
    }

    @Composable
    fun PegawaiHome() {
        val navController = rememberNavController()
        val scaffoldState: ScaffoldState = rememberScaffoldState()
        val coroutineScope: CoroutineScope = rememberCoroutineScope()
        Scaffold(
            bottomBar = { BotNavPegawai(navController = navController) },
            topBar = { TopAppBarPegawai() }, scaffoldState = scaffoldState
        ) {
            Column(modifier = Modifier.padding(it)) {
                NavigationGraph(navController = navController, scaffoldState, coroutineScope)
            }
        }
    }

    @Composable
    fun BotNavPegawai(navController: NavController) {
        val items = listOf(BotNavItemPegawai.ListBooking, BotNavItemPegawai.ListBarang)
        BottomNavigation(
            backgroundColor = MaterialTheme.colors.onPrimary,
            contentColor = MaterialTheme.colors.primary
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        Image(
                            painter = painterResource(id = item.icon),
                            contentDescription = stringResource(id = item.title),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    label = { Text(stringResource(item.title)) },
                    alwaysShowLabel = true,
                    selected = currentRoute == item.route,
                    selectedContentColor = MaterialTheme.colors.primary,
                    unselectedContentColor = Color.Gray,
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
            }
        }
    }

    @Composable
    fun TopAppBarPegawai() {
        val context = LocalContext.current
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.homepage_pegawai),
                    fontWeight = FontWeight.Bold
                )
            },
            actions = {
                IconButton(onClick = {
                    auth.signOut()
                    Intent(context, HomeActivity::class.java).also { intent ->
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = stringResource(id = R.string.keluar)
                    )
                }
            })
    }

    @Preview
    @Composable
    fun TopAppBarPegawaiPreview() {
        MalangCampTheme {
            TopAppBarPegawai()
        }
    }

    @Preview
    @Composable
    fun PegawaiHomePreview() {
        MalangCampTheme {
            PegawaiHome()
        }
    }

    @Preview
    @Composable
    fun BotNavPegawaiPreview() {
        MalangCampTheme {
            val navController = rememberNavController()
            BotNavPegawai(navController)
        }
    }
}
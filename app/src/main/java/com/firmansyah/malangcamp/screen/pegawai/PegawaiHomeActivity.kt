package com.firmansyah.malangcamp.screen.pegawai

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.rememberNavController
import com.firmansyah.malangcamp.HomeActivity
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.component.BotNav
import com.firmansyah.malangcamp.screen.BotNavItem
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
            bottomBar = {
                BotNav(
                    navController = navController,
                    items = listOf(BotNavItem.ListBookingScreen, BotNavItem.ListBarangScreen)
                )
            },
            topBar = { TopAppBarPegawai() }, scaffoldState = scaffoldState
        ) {
            Column(modifier = Modifier.padding(it)) {
                NavigationGraph(navController = navController, scaffoldState, coroutineScope, true)
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
}
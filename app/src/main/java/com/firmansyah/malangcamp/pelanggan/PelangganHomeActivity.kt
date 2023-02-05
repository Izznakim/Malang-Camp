package com.firmansyah.malangcamp.pelanggan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.rememberNavController
import com.firmansyah.malangcamp.HomeActivity
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.component.BotNav
import com.firmansyah.malangcamp.other.ConstVariable.Companion.EXTRA_UID
import com.firmansyah.malangcamp.pelanggan.ui.menu.PelangganAkunActivity
import com.firmansyah.malangcamp.pelanggan.ui.menu.PeraturanSewaActivity
import com.firmansyah.malangcamp.screen.BotNavItem
import com.firmansyah.malangcamp.screen.NavigationGraph
import com.firmansyah.malangcamp.theme.MalangCampTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope

//  Halaman home pelanggan untuk menampung fragmen
class PelangganHomeActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        setContent {
            MalangCampTheme {
                PelangganHome()
            }
        }
    }

    @Composable
    fun PelangganHome() {
        val navController = rememberNavController()
        val scaffoldState: ScaffoldState = rememberScaffoldState()
        val coroutineScope: CoroutineScope = rememberCoroutineScope()
        Scaffold(bottomBar = {
            BotNav(
                navController = navController,
                items = listOf(
                    BotNavItem.ListBarangSewaScreen,
                    BotNavItem.PembayaranScreen,
                    BotNavItem.RiwayatPemesananScreen
                )
            )
        }, topBar = { TopAppBarPelanggan() }, scaffoldState = scaffoldState) {
            Column(modifier = Modifier.padding(it)) {
                NavigationGraph(
                    navController = navController,
                    scaffoldState = scaffoldState,
                    coroutineScope = coroutineScope,
                    pegawai = false
                )
            }
        }
    }

    @Composable
    fun TopAppBarPelanggan() {
        val context = LocalContext.current
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.homepage_pelanggan),
                    fontWeight = FontWeight.Bold
                )
            },
            actions = {
                AccountIcon(context)
                DropDownMenu(context)
            }
        )
    }

    @Composable
    private fun DropDownMenu(context: Context) {
        var showMenu by remember { mutableStateOf(false) }
        IconButton(onClick = {
            showMenu = !showMenu
        }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = stringResource(R.string.drowpdown_menu)
            )
        }
        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
            DropdownMenuItem(onClick = {
                Intent(context, PeraturanSewaActivity::class.java).also { intent ->
                    context.startActivity(intent)
                }
            }) {
                Text(text = stringResource(R.string.aturan_sewa))
            }
            DropdownMenuItem(onClick = {
                auth.signOut()
                Intent(context, HomeActivity::class.java).also { intent ->
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                }
            }) {
                Text(text = stringResource(R.string.keluar))
            }
        }
    }

    @Composable
    private fun AccountIcon(context: Context) {
        IconButton(onClick = {
            val idAuth = auth.currentUser?.uid
            Intent(context, PelangganAkunActivity::class.java).also { intent ->
                intent.putExtra(EXTRA_UID, idAuth)
                context.startActivity(intent)
            }
        }) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = stringResource(id = R.string.akun)
            )
        }
    }
}
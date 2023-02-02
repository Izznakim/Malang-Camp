package com.firmansyah.malangcamp.screen.pegawai

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.component.*
import com.firmansyah.malangcamp.theme.MalangCampTheme

//  Halaman login sebagai pegawai
class PegawaiLoginActivity : ComponentActivity() {
    private val viewModel by viewModels<PegawaiLoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MalangCampTheme {
                PegawaiLogin(viewModel)
            }
        }
    }
}

@Composable
private fun PegawaiLogin(viewModel: PegawaiLoginViewModel) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var emailError by rememberSaveable { mutableStateOf(true) }
    var passwordError by rememberSaveable { mutableStateOf(true) }
    val context = LocalContext.current

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewModel.isLoading.value) {
                LinearProgressBar(1f)
            } else {
                LinearProgressBar(0f)
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp, top = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Logo(
                    drwbl = R.drawable.mountains,
                    contentDesc = R.string.login_pegawai,
                    modifier = Modifier.size(146.dp)
                )
                TextLogin(stringResource(id = R.string.login_pegawai))
                emailError = emailInput(email = email.trim(), onEmailValueChange = { newValue ->
                    email = newValue.trim()
                })
                passwordError = passwordInput(password = password.trim(),
                    onPasswordValueChange = { newValue -> password = newValue.trim() })
                ButtonPegawaiLogin(viewModel, email, password, emailError, passwordError, context)

                if (viewModel.isIntent.value) {
                    Intent(context, PegawaiHomeActivity::class.java).also { intent ->
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}

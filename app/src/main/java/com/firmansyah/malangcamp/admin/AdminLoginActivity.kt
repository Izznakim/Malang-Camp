package com.firmansyah.malangcamp.admin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.firmansyah.malangcamp.component.*
import com.firmansyah.malangcamp.theme.MalangCampTheme

//  Halaman login sebagai admin
class AdminLoginActivity : ComponentActivity() {
    private val loginViewModel by viewModels<AdminLoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MalangCampTheme {
                AdminLogin(loginViewModel)
            }
        }
    }
}

@Composable
private fun AdminLogin(viewModel: AdminLoginViewModel) {
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
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LogoLoginAdmin()
                LoginAdmin()
                emailError = emailInput(email = email.trim(), onEmailValueChange = { newValue ->
                    email = newValue.trim()
                })
                passwordError = passwordInput(password = password.trim(),
                    onPasswordValueChange = { newValue -> password = newValue.trim() })
                ButtonAdminLogin(viewModel, email, password, emailError, passwordError)

                if (viewModel.isIntent.value) {
                    Intent(context, AdminHomeActivity::class.java).also { intent ->
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}

package com.firmansyah.malangcamp.pelanggan

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.component.*
import com.firmansyah.malangcamp.theme.MalangCampTheme

//  Halaman register sebagai pelanggan
class PelangganRegisterActivity : ComponentActivity() {
    private val viewModel by viewModels<PelangganRegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MalangCampTheme {
                var username by rememberSaveable { mutableStateOf("") }
                var namaDepan by rememberSaveable { mutableStateOf("") }
                var namaBelakang by rememberSaveable { mutableStateOf("") }
                var nomorTelepon by rememberSaveable { mutableStateOf("") }
                var email by rememberSaveable { mutableStateOf("") }
                var password by rememberSaveable { mutableStateOf("") }
                var ulangiPassword by rememberSaveable { mutableStateOf("") }

                var usernameError by rememberSaveable { mutableStateOf(false) }
                var namaDepanError by rememberSaveable { mutableStateOf(false) }
                var namaBelakangError by rememberSaveable { mutableStateOf(false) }
                var nomorTeleponError by rememberSaveable { mutableStateOf(false) }
                var emailError by rememberSaveable { mutableStateOf(false) }
                var passwordError by rememberSaveable { mutableStateOf(false) }
                var ulangiPasswordError by rememberSaveable { mutableStateOf(false) }

                val context = LocalContext.current

                val usernameIsEmpty = username.isEmpty()

                val namaDepanIsEmpty = namaDepan.isEmpty()
                val namaBelakangIsEmpty = namaBelakang.isEmpty()

                val ulangiPasswordNotMatch = ulangiPassword != password

                val isNotError =
                    !(usernameError || namaDepanError || namaBelakangError || nomorTeleponError || emailError || passwordError || ulangiPasswordError)

                Surface {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (viewModel.isLoading.value) {
                            LinearProgressBar(alpha = 1f)
                        } else {
                            LinearProgressBar(alpha = 0f)
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Logo(
                                drwbl = R.drawable.hiking,
                                contentDesc = R.string.register_pelanggan,
                                modifier = Modifier
                                    .size(124.dp)
                                    .padding(top = 16.dp)
                                    .clip(CircleShape)
                            )
                            TextLoginRegister(text = stringResource(id = R.string.daftar))
                            usernameError = registerInput(
                                profile = username,
                                onProfileValueChange = { newValue -> username = newValue },
                                trim = true,
                                contentDescription = stringResource(R.string.username_error),
                                placeHolder = stringResource(id = R.string.username),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                paddingTop = 32.dp,
                                isError = usernameIsEmpty,
                                errorText = stringResource(R.string.username_harus_diisi)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .padding(end = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    namaDepanError = registerInput(
                                        profile = namaDepan,
                                        onProfileValueChange = { newValue -> namaDepan = newValue },
                                        trim = false,
                                        contentDescription = stringResource(R.string.nama_depan_error),
                                        placeHolder = stringResource(id = R.string.nama_depan),
                                        keyboardOptions = KeyboardOptions(
                                            capitalization = KeyboardCapitalization.Words,
                                            imeAction = ImeAction.Next
                                        ),
                                        paddingTop = 0.dp,
                                        isError = namaDepanIsEmpty,
                                        errorText = stringResource(R.string.nama_depan_harus_diisi)
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .padding(start = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    namaBelakangError = registerInput(
                                        profile = namaBelakang,
                                        onProfileValueChange = { newValue ->
                                            namaBelakang = newValue
                                        },
                                        trim = false,
                                        contentDescription = stringResource(R.string.nama_belakang_error),
                                        placeHolder = stringResource(id = R.string.nama_belakang),
                                        keyboardOptions = KeyboardOptions(
                                            capitalization = KeyboardCapitalization.Words,
                                            imeAction = ImeAction.Next
                                        ),
                                        paddingTop = 0.dp,
                                        isError = namaBelakangIsEmpty,
                                        errorText = stringResource(R.string.nama_belakang_harus_diisi)
                                    )
                                }
                            }
                            nomorTeleponError = nomorTeleponInput(
                                nomorTelepon = nomorTelepon,
                                onNoTelpValueChange = { newValue -> nomorTelepon = newValue }
                            )
                            emailError = emailInput(
                                email = email.trim(),
                                onEmailValueChange = { newValue -> email = newValue.trim() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            )
                            passwordError = passwordInput(
                                password = password.trim(),
                                onPasswordValueChange = { newValue -> password = newValue.trim() })
                            ulangiPasswordError = registerInput(
                                profile = ulangiPassword,
                                onProfileValueChange = { newValue -> ulangiPassword = newValue },
                                trim = true,
                                contentDescription = stringResource(R.string.ulangi_password_error),
                                placeHolder = stringResource(id = R.string.ulangi_password),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done
                                ),
                                paddingTop = 8.dp,
                                isError = ulangiPasswordNotMatch,
                                visualTransformation = PasswordVisualTransformation(),
                                errorText = stringResource(R.string.password_tidak_cocok)
                            )
                            ButtonPelangganRegister(
                                viewModel,
                                username,
                                email,
                                namaDepan,
                                namaBelakang,
                                nomorTelepon,
                                password,
                                isNotError,
                                context
                            )
                            TextToLogin(context)

                            if (viewModel.isIntent.value) {
                                Intent(context, PelangganHomeActivity::class.java).also { intent ->
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    context.startActivity(intent)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
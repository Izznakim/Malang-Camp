package com.firmansyah.malangcamp.component

import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.firmansyah.malangcamp.R

@Composable
fun emailInput(
    email: String,
    onEmailValueChange: (newValue: String) -> Unit,
    modifier: Modifier
): Boolean {
    val isEmpty = email.isEmpty()
    val isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isError = !isValid || isEmpty

    TextField(
        value = email,
        onValueChange = { newValue ->
            onEmailValueChange(
                newValue
            )
        },
        trailingIcon = {
            if (isEmpty) {
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = stringResource(R.string.e_mail_error),
                    tint = MaterialTheme.colors.error
                )
            } else if (!isValid) {
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = stringResource(R.string.e_mail_error),
                    tint = MaterialTheme.colors.error
                )
            }
        },
        isError = isError,
        placeholder = { Text(text = stringResource(id = R.string.email)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        modifier = modifier
    )

    if (isEmpty) {
        ErrorText(stringResource(R.string.email_harus_diisi))
    } else if (!isValid) {
        ErrorText(stringResource(R.string.email_tidak_valid))
    }
    return isError
}

@Composable
fun passwordInput(password: String, onPasswordValueChange: (newValue: String) -> Unit): Boolean {
    var isNotEnough = password.isEmpty() || password.length < 6
    val isError = isNotEnough

    TextField(
        value = password,
        onValueChange = { newValue ->
            onPasswordValueChange(newValue)
            isNotEnough = false
        },
        trailingIcon = {
            if (isNotEnough) {
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = stringResource(R.string.password_error),
                    tint = MaterialTheme.colors.error
                )
            }
        },
        isError = isNotEnough,
        placeholder = { Text(text = stringResource(id = R.string.password)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        visualTransformation = PasswordVisualTransformation()
    )
    if (isNotEnough) {
        ErrorText(stringResource(R.string.password_harus_lebih_dari6_karakter))
    }
    return isError
}

@Composable
fun editTextDeskBarang(
    deskBarang: String,
    trim: Boolean,
    placeholderText: String,
    titleDesk: String,
    keyboardOptions: KeyboardOptions,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    minLines: Int = 1,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(top = 8.dp),
    textStyle: TextStyle = LocalTextStyle.current
): String {
    var deskBrng by rememberSaveable { mutableStateOf(deskBarang) }
    Column(modifier = modifier) {
        TextField(
            value = if (trim) deskBrng.trim() else deskBrng,
            onValueChange = { newValue ->
                deskBrng = if (trim) newValue.trim() else newValue
            },
            trailingIcon = {
                if (deskBrng.isEmpty()) {
                    Icon(
                        Icons.Filled.Warning,
                        contentDescription = stringResource(R.string.deskbarang_error, titleDesk),
                        tint = MaterialTheme.colors.error
                    )
                }
            },
            isError = deskBrng.isEmpty(),
            label = { Text(text = placeholderText, softWrap = true) },
            placeholder = { Text(text = placeholderText, softWrap = true) },
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            modifier = Modifier.fillMaxWidth(), maxLines = maxLines, minLines = minLines,
            textStyle = textStyle
        )
        if (deskBrng.isEmpty()) {
            ErrorText(stringResource(R.string.deskbarang_harus_diisi, titleDesk))
        }
    }
    return deskBrng
}

@Composable
fun nomorTeleponInput(
    nomorTelepon: String,
    onNoTelpValueChange: (newValue: String) -> Unit
): Boolean {
    val nomorTeleponIsEmpty = nomorTelepon.isEmpty()
    val nomorTeleponIsNotValid = !Patterns.PHONE.matcher(nomorTelepon).matches()
    val isError = nomorTeleponIsEmpty || nomorTeleponIsNotValid

    TextField(
        value = nomorTelepon.trim(),
        onValueChange = { newValue -> onNoTelpValueChange(newValue.trim()) },
        trailingIcon = {
            if (nomorTeleponIsEmpty) {
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = stringResource(R.string.nomor_telepon_error),
                    tint = MaterialTheme.colors.error
                )
            } else if (nomorTeleponIsNotValid) {
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = stringResource(R.string.nomor_telepon_error),
                    tint = MaterialTheme.colors.error
                )
            }
        },
        isError = isError,
        placeholder = { Text(text = stringResource(id = R.string.nomor_telepon)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    )
    if (nomorTeleponIsEmpty) {
        ErrorText(text = stringResource(R.string.nomor_telepon_harus_diisi))
    } else if (nomorTeleponIsNotValid) {
        ErrorText(text = stringResource(R.string.nomor_telepon_tidak_valid))
    }
    return isError
}

@Composable
fun registerInput(
    profile: String,
    onProfileValueChange: (newValue: String) -> Unit,
    trim: Boolean,
    contentDescription: String,
    placeHolder: String,
    keyboardOptions: KeyboardOptions,
    paddingTop: Dp,
    isError: Boolean,
    errorText: String,
    visualTransformation: VisualTransformation = VisualTransformation.None
): Boolean {
//    var regProfile by rememberSaveable { mutableStateOf(profile) }
    TextField(
        value = if (trim) profile.trim() else profile,
        onValueChange = { newValue -> onProfileValueChange(if (trim) newValue.trim() else newValue) },
        trailingIcon = {
            if (isError) {
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = contentDescription,
                    tint = MaterialTheme.colors.error
                )
            }
        },
        isError = isError,
        placeholder = { Text(text = placeHolder) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = paddingTop)
    )
    if (isError) {
        ErrorText(text = errorText)
    }
    return isError
}